package com.dkelava.backgammon.trainer.model;

import java.util.Collection;

import com.google.common.collect.EvictingQueue;

import org.simpleframework.xml.Attribute;

public class PlayerStatistic {

    @Attribute
    private int wins = 0;
    @Attribute
    private int losses = 0;
    @Attribute
    private int examples = 0;

    EvictingQueue<Boolean> results = EvictingQueue.create(1000);
    EvictingQueue<Double> winRatios = EvictingQueue.create(10000);


    public void reset() {
        wins = 0;
        losses = 0;
        examples = 0;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getTotalGames() {
        return wins + losses;
    }

    public int getExamples() {
        return examples;
    }

    public double getWinPercent() {
        return getTotalGames() > 0 ? 100.0 * (double)wins / (double)getTotalGames() : 0.0;
    }

    public double getRecentWinPercent() {
        int sum = 0;
        for(boolean isWin : results) {
            if(isWin) {
                ++sum;
            }
        }
        return (double) sum / (double) results.size();
    }

    public void add(boolean isWin) {
        results.add(isWin);
        if(isWin) {
            ++wins;
        } else {
            ++losses;
        }
    }

    public Collection<Double> getRecentWinRatios() {
        return winRatios;
    }

    public void addExample() {
        ++examples;
        winRatios.add(getRecentWinPercent());
    }
}
