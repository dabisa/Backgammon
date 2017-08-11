package com.dkelava.backgammon.trainer.model;

interface GameGenerator {
    boolean hasNext();
    Game generateNext();
}
