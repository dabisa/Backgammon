package com.dkelava.backgammon.trainer.model;

import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Root
public class BackgammonTrainer {

    private int iteration = 0;

    @ElementList(inline=true)
    private List<Player> players = new LinkedList<>();

    private Observer observer;

    public static BackgammonTrainer restore() {
        Serializer serializer = new Persister();
        File source = new File("bgt.xml");

        try {
            return serializer.read(BackgammonTrainer.class, source);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private void update() {
        if(observer != null) {
            observer.onUpdate();
        }
    }

    private boolean isDone() {
        if(observer != null) {
            return observer.isDone();
        } else {
            return false;
        }
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    public void add(Player player) {
        players.add(player);
    }

    public void remove(int i) {
        players.remove(i);
    }

    public Player get(int i) {
        return players.get(i);
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public int size() {
        return players.size();
    }

    public int getIteration() {
        return iteration;
    }

    public XYDataset createDataSet( ) {
        final XYSeriesCollection dataSet = new XYSeriesCollection( );
        for(Player player : players) {
            final XYSeries series = new XYSeries(player.getName());
            int i = 0;
            for(double winRatio : player.getPlayerStatistic().getRecentWinRatios()) {
                series.add(i, winRatio);
                ++i;
            }
            dataSet.addSeries( series );
        }
        return dataSet;
    }

    public void play() throws Exception {
        while (!isDone()) {
            GameGenerator gameGenerator = EliminationSystemGameGenerator.generate(players);
            while(!isDone() && gameGenerator.hasNext()) {
                Game game = gameGenerator.generateNext();

                for(Player player : players) {
                    if(player.getLearningParameters().isLearning()) {
                        if(player.getLearningParameters().useAllGames() || game.isPlayer(player)) {
                            player.learn(game.getHistory());
                        }
                    }
                }
                update();
            }
            ++iteration;
            update();
        }
    }

    private void resetStatistics() {
        iteration = 0;
        for(Player player : players) {
            player.getPlayerStatistic().reset();
        }
        update();
    }

    public void save() {
        // Save configuration
        Serializer serializer = new Persister();
        File result = new File("bgt.xml");

        try {
            serializer.write(this, result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Save players
        try {
            for(Player player : players) {
                player.save();
            }
            update();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public interface Observer {
        boolean isDone();
        void onUpdate();
    }
}
