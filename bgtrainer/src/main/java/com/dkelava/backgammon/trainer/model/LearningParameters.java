package com.dkelava.backgammon.trainer.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root
public class LearningParameters {

    public static double DEFAULT_ALPHA = 0.1;
    public static double DEFAULT_LAMBDA = 0.96;

    @Attribute
    private double alpha = DEFAULT_ALPHA;
    @Attribute
    private double lambda = DEFAULT_LAMBDA;
    @Attribute
    private boolean isLearningEnabled = true;
    @Attribute
    private boolean useAllGames = true;

    public LearningParameters() {}

    private LearningParameters(Builder builder) {
        this.alpha = builder.alpha;
        this.lambda = builder.lambda;
        this.isLearningEnabled = builder.isLearningEnabled;
        this.useAllGames = builder.useAllGames;
    }

    public double getAlpha() {
        return alpha;
    }

    public double getLambda() {
        return lambda;
    }

    public boolean isLearning() {
        return isLearningEnabled;
    }

    public boolean useAllGames() {
        return useAllGames;
    }

    public static class Builder {

        private double alpha = DEFAULT_ALPHA;
        private double lambda = DEFAULT_LAMBDA;
        private boolean isLearningEnabled = true;
        private boolean useAllGames = true;

        public Builder() {}

        public Builder(LearningParameters param) {
            this.alpha = param.alpha;
            this.lambda = param.lambda;
            this.isLearningEnabled = param.isLearningEnabled;
            this.useAllGames = param.useAllGames;
        }

        public Builder setAlpha(double alpha) {
            this.alpha = alpha;
            return this;
        }
        public Builder setLambda(double lambda) {
            this.lambda = lambda;
            return this;
        }
        public Builder enableLearning(boolean isLearningEnabled) {
            this.isLearningEnabled = isLearningEnabled;
            return this;
        }
        public Builder setUseAllGames(boolean useAllGames) {
            this.useAllGames = useAllGames;
            return this;
        }

        public LearningParameters build() {
            return new LearningParameters(this);
        }
    }
}
