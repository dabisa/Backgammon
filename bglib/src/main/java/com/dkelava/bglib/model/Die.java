package com.dkelava.bglib.model;

/**
 * Die represents a die used in a game.
 */
public class Die {

    private Strategy strategy;
    private Face face = Face.None;

    /**
     * Creates a die with specified strategy for rolling
     */
    public Die(Strategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Roll die
     */
    public Face roll(Face face) {
        if(face == null) {
            this.face = strategy.roll();
        } else {
            this.face = face;
        }
        return this.face;
    }

    /**
     * Clear die state
     */
    public void clear() {
        face = Face.None;
    }

    /**
     * Get current state of this die
     */
    public Face getFace() {
        return face;
    }

    /**
     * Set current state of this die
     */
    public void setFace(Face face) {
        this.face = face;
    }

    /**
     * Set rolling strategy of this die
     */
    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Represents die state
     *
     * @note Do not change order!
     */
    public enum Face {
        None,
        Die1,
        Die2,
        Die3,
        Die4,
        Die5,
        Die6;

        /**
         * Returns Face that is represented by provided value.
         *
         * @param value Value to be decoded
         * @return Face that represents decoded value
         * @throws Exception when provided value is not a valid code for Face.
         */
        public static Face decode(String value) throws Exception {
            switch(value) {
                case "0": return None;
                case "1": return Die1;
                case "2": return Die2;
                case "3": return Die3;
                case "4": return Die4;
                case "5": return Die5;
                case "6": return Die6;
                default: throw new IllegalArgumentException("invalid die face code");
            }
        }

        /**
         * Returns Face that corresponds to specified value.
         */
        public static Face valueOf(int value) {
            if (value < 1 || value > 6) {
                throw new IllegalArgumentException("Illegal die value");
            }

            return Face.values()[value];
        }

        /**
         * Encodes Face as a string value
         *
         * @return String that represents encoded Face
         */
        public String encode() {
            return Integer.toString(getValue());
        }

        /**
         * Returns integer value that corresponds to die face
         */
        public final int getValue() {
            return this.ordinal();
        }
    }

    /**
     * Represents strategy used to roll a die.
     * Single method in interface returns new die state after die is rolled.
     */
    public interface Strategy {
        Face roll();
    }
}
