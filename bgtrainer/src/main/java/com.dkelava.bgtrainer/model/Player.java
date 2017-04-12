package com.dkelava.bgtrainer.model;

import com.dkelava.bglib.game.History;
import com.dkelava.bglib.nn.BackgammonNeuralNetwork;
import com.dkelava.bglib.nn.SimpleInputCoder;
import com.dkelava.bglib.nn.SimpleOutputCoder;
import com.dkelava.bglib.strategy.Strategy;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.File;
import java.io.IOException;

@Root
public class Player {

    @Attribute
    private String name;
    @Attribute
    private String path;
    private BackgammonNeuralNetwork trainer;
    @Element
    private LearningParameters learningParameters = new LearningParameters();
    @Element
    private PlayerStatistic playerStatistic = new PlayerStatistic();
    private boolean isChanged = true;


    public Player(String name, String path, BackgammonNeuralNetwork trainer) {
        this.name = name;
        this.path = path;
        this.trainer = trainer;
    }

    public Player(@Attribute (name = "name") String name, @Attribute (name = "path") String path) {
        this.name = name;
        this.path = path;
        try {
            this.trainer = BackgammonNeuralNetwork.load(new SimpleInputCoder(), new SimpleOutputCoder(), new File(path));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        this.isChanged = false;
    }

    public static Player create(String name, String path, int hiddenLayerSize) {
        BackgammonNeuralNetwork trainer = BackgammonNeuralNetwork.create(new SimpleInputCoder(), new SimpleOutputCoder(), hiddenLayerSize);
        return new Player(name, path, trainer);
    }

    public static Player load(String name, String path) throws IOException {
        return new Player(name, path);
    }

    private static String getPath(String name) {
        return name + ".dat";
    }

    @Override
    public String toString() {
        return name + ": " + this.getPlayerStatistic();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public boolean isConfigured() {
        return (trainer != null && name != null);
    }

    public String getPath() {
        return this.path != null ? this.path : getPath(name);
    }

    public void save() throws IOException {
        if (isConfigured()) {
            trainer.save(getPath());
            isChanged = false;
        } else {
            throw new RuntimeException("Player not configured");
        }
    }

    public void save(String path) throws IOException {
        if (isConfigured()) {
            this.path = path;
            trainer.save(path);
            isChanged = false;
        } else {
            throw new RuntimeException("Player not configured");
        }
    }

    public Strategy getStrategy() {
        if (isConfigured()) {
            return trainer.getStrategy();
        } else {
            throw new RuntimeException("Player not configured");
        }
    }

    public LearningParameters getLearningParameters() {
        return learningParameters;
    }

    public void setLearningParameters(LearningParameters learningParameters) {
        this.learningParameters = learningParameters;
    }

    public PlayerStatistic getPlayerStatistic() {
        return playerStatistic;
    }

    public void learn(History history) {
        if (isConfigured()) {
            if (learningParameters.isLearning()) {
                try {
                    this.trainer.learn(history, learningParameters.getAlpha(), learningParameters.getLambda());
                    isChanged = true;
                    playerStatistic.addExample();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            throw new RuntimeException("Player not configured");
        }
    }

    public boolean isChanged() {
        return isChanged;
    }
}
