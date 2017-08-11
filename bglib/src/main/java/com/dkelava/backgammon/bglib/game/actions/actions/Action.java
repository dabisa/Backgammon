package com.dkelava.backgammon.bglib.game.actions.actions;

import com.dkelava.backgammon.bglib.game.Game;
import com.dkelava.backgammon.bglib.model.Backgammon;

import java.util.HashMap;
import java.util.Map;

public abstract class Action {

    protected interface Decoder {
        Action decode(String value) throws Exception;
    }

    private static Map<String,Decoder> decoders = new HashMap<>();

    protected static void addDecoder(String id, Decoder decoder) {
        decoders.put(id, decoder);
    }

    public static Action decode(String value) throws Exception {
        for(Map.Entry<String, Decoder> entry : decoders.entrySet()) {
            if(value.substring(0, 1).equals(entry.getKey())) {
                return entry.getValue().decode(value);
            }
        }
        throw new IllegalArgumentException("invalid action encoding");
    }

    public abstract void execute(Backgammon backgammon, Game.Observer observer) throws Exception;

    public abstract void undo(Backgammon backgammon, Game.Observer observer);

    public abstract String encode();
}
