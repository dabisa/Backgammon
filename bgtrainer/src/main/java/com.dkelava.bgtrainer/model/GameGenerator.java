package com.dkelava.bgtrainer.model;

interface GameGenerator {
    boolean hasNext();
    Game generateNext();
}
