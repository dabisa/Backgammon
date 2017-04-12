package com.dkelava.bglib.nn;

import com.dkelava.bglib.game.actions.actions.Action;
import com.dkelava.bglib.game.History;
import com.dkelava.bglib.model.Backgammon;
import com.dkelava.bglib.model.BackgammonState;
import com.dkelava.bglib.model.Color;
import com.dkelava.bglib.model.Status;
import com.dkelava.bglib.strategy.MaximizeValueStrategy;
import com.dkelava.bglib.strategy.Strategy;
import com.google.common.collect.ImmutableList;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.Updater;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class BackgammonNeuralNetwork {

    private MultiLayerNetwork model;
    private InputCoder inputCoder;
    private OutputCoder outputCoder;

    private BackgammonNeuralNetwork(MultiLayerNetwork model, InputCoder inputCoder, OutputCoder outputCoder) {
        this.model = model;
        this.inputCoder = inputCoder;
        this.outputCoder = outputCoder;
    }

    public static BackgammonNeuralNetwork create(InputCoder inputCoder, OutputCoder outputCoder, int hidden) {

        int seed = 0;
        double alpha = 0.1;
        int nInputs = inputCoder.getInputSize();
        int nHidden = hidden;
        int nOutput = outputCoder.getSize();

        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .iterations(1)
                .learningRate(alpha)
                .updater(Updater.NONE)
                .regularization(false)
                .list()
                .layer(0, new DenseLayer.Builder()
                    .nIn(nInputs)
                    .nOut(nHidden)
                    .activation(Activation.SIGMOID)
                    .weightInit(WeightInit.UNIFORM)
                    .build())
                .layer(1, new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                    .nIn(nHidden)
                    .nOut(nOutput)
                    .activation(Activation.SIGMOID)
                    .weightInit(WeightInit.UNIFORM)
                    .build())
                .pretrain(false)
                .backprop(true)
                .build();

        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();

        return new BackgammonNeuralNetwork(model, inputCoder, outputCoder);
    }

    public static BackgammonNeuralNetwork load(InputCoder inputCoder, OutputCoder outputCoder, File path) throws IOException {
        MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(path);
        return new BackgammonNeuralNetwork(model, inputCoder, outputCoder);
    }

    public static BackgammonNeuralNetwork load(InputCoder inputCoder, OutputCoder outputCoder, InputStream is) throws IOException {
        MultiLayerNetwork model = ModelSerializer.restoreMultiLayerNetwork(is);
        return new BackgammonNeuralNetwork(model, inputCoder, outputCoder);
    }

    private double evaluate(BackgammonState state, Color color) {
        // TODO: 3.3.2017. use strategy or template method
        return model.output(inputCoder.encode(state, color)).getDouble(0);
    }

    public void learn(History history, double alpha, double lambda) throws Exception {
        ImmutableList<Action> moves = history.getActions();
        Backgammon backgammon = new Backgammon();
        for(Action action : moves) {
            action.execute(backgammon, null);
        }
        learn2(backgammon, moves, alpha, lambda);
    }

    private void learn2(Backgammon backgammon, ImmutableList<Action> actions, double alpha, double lambda) throws Exception {
        if( backgammon.getState().getWinner() == Color.None ) {
            throw new IllegalStateException("Not game end");
        }

        INDArray A_white = outputCoder.encode(backgammon.getState(), Color.White);
        INDArray A_black = outputCoder.encode(backgammon.getState(), Color.Black);
        double A_lambda = 1;
        int k = 1;

        /*
        List<INDArray> inputVectors = new LinkedList<>();
        List<INDArray> outputVectors = new LinkedList<>();
        */

        for(Action action :actions.reverse()) {
            action.undo(backgammon, null);
            if (backgammon.getState().getStatus() == Status.Rolling) {

                INDArray X_white = inputCoder.encode(backgammon.getState(), Color.White);
                INDArray X_black = inputCoder.encode(backgammon.getState(), Color.Black);

                INDArray Y_white = A_white.dup().div(A_lambda);
                INDArray Y_black = A_black.dup().div(A_lambda);

                /*
                inputVectors.add(X_white);
                inputVectors.add(X_black);
                outputVectors.add(Y_white);
                outputVectors.add(Y_black);
                */

                INDArray O_white = model.output(X_white);
                INDArray O_black = model.output(X_black);

                model.fit(X_white, Y_white);
                model.fit(X_black, Y_black);



                A_white = A_white.mul(lambda).add(O_white);
                A_black = A_black.mul(lambda).add(O_black);
                A_lambda = A_lambda + Math.pow(lambda, k);
            }
        }

        /*
        DataSet dataSet = new DataSet(Nd4j.vstack(inputVectors), Nd4j.vstack(outputVectors));
        model.fit(dataSet);
        */
    }

    public Strategy getStrategy() {
        return new MaximizeValueStrategy() {
            @Override
            public double evaluate(BackgammonState state) {
                return BackgammonNeuralNetwork.this.evaluate(state, state.getCurrentPlayer());
            }
        };
    }

    public void save(String path) throws IOException {
        ModelSerializer.writeModel(model, new File(path), true);
    }
}
