package com.dkelava.backgammon.bglib.game;

import com.dkelava.backgammon.bglib.game.actions.actions.Action;
import com.google.common.collect.ImmutableList;

import java.util.LinkedList;
import java.util.ListIterator;

public class History {
    private LinkedList<Action> history = new LinkedList<Action>();
    private ListIterator<Action> current = history.listIterator();

    public History() {
    }

    public void restore(String history) throws Exception {
        clear();
        String[] tokens = history.split(";");
        int n = 0;
        int cur = -1;
        for(String token : tokens) {
            if(token.equals("*")) {
                cur = n;
            } else {
                add(Action.decode(token));
            }
            ++n;
        }
        if(cur > 0) {
            current = this.history.listIterator(cur);
        } else {
            current = this.history.listIterator(this.history.size());
        }
    }

    public String encode() {
        StringBuilder builder = new StringBuilder();
        ListIterator<Action> it = history.listIterator();
        while(it.hasNext()) {
            if(it.nextIndex()== current.nextIndex()) {
                builder.append("*;");
            }
            Action action = it.next();
            builder.append(action.encode() + ";");
        }
        return builder.toString();
    }

    public void clear() {
        history.clear();
        current = history.listIterator();
    }

    public boolean hasPrevious() {
        return current.hasPrevious();
    }

    public boolean hasNext() {
        return current.hasNext();
    }

    public Action getPrevious() {
        return current.previous();
    }

    public Action getNext() {
        return current.next();
    }

    public void add(Action action) {
        int index = current.nextIndex();
        history.subList(index, history.size()).clear();
        history.add(action);
        current = history.listIterator(history.size());
    }

    public ImmutableList<Action> getActions() {
        return ImmutableList.copyOf(history);
    }
}