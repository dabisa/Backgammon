package com.dkelava.backgammon.trainer.model;

import com.dkelava.backgammon.bglib.game.actions.actions.Action;
import com.dkelava.backgammon.bglib.game.History;
import com.dkelava.backgammon.bglib.model.Backgammon;
import com.dkelava.backgammon.bglib.model.Color;
import com.dkelava.backgammon.bglib.model.Status;

public class Game {
    private Player player1;
    private Player player2;
    private Player winner = null;
    private History history = null;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public Player getWhitePlayer() {
        return player1;
    }

    public Player getBlackPlayer() {
        return player2;
    }

    public Player getWinner() {
        return winner;
    }

    public void play(Backgammon backgammon) throws Exception {
        history = new History();

        while (backgammon.getState().getStatus() != Status.End) {
            Action action = null;
            switch(backgammon.getState().getCurrentPlayer()) {
                case White:
                    action = player1.getStrategy().play(backgammon.getState());
                    break;
                case Black:
                    action = player2.getStrategy().play(backgammon.getState());
                    break;
            }
            history.add(action);
            action.execute(backgammon, null);
        }

        player1.getPlayerStatistic().add(backgammon.getState().getWinner() == Color.White);
        player2.getPlayerStatistic().add(backgammon.getState().getWinner() == Color.Black);
        switch (backgammon.getState().getWinner()) {
            case White:
                winner = player1;
                break;

            case Black:
                winner = player2;
                break;
        }
    }

    public History getHistory() {
        return history;
    }

    public boolean isPlayer(Player player) {
        return getBlackPlayer() == player || getWhitePlayer() == player;
    }
}
