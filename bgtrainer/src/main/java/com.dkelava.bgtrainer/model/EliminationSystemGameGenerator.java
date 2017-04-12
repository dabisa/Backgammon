package com.dkelava.bgtrainer.model;

import com.dkelava.bglib.model.Backgammon;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.TreeTraverser;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class EliminationSystemGameGenerator implements GameGenerator {

    private Iterator it;
    Node node;

    private static class Node {
        private Player player = null;
        private Node first = null;
        private Node second = null;

        public Node() {
        }

        public Node(Player player) {
            this.player = player;
        }

        public Node(Node first, Node second) {
            this.first = first;
            this.second = second;
        }

        public Player getPlayer() {
            return player;
        }

        public void setPlayer(Player player) {
            this.player = player;
        }

        public boolean isLeaf() {
            return first == null && second == null;
        }

        public Node getFirst() {
            return first;
        }

        public Node getSecond() {
            return second;
        }
    }

    private static class PlayerTraverser extends TreeTraverser<Node> {
        @Override
        public Iterable<Node> children(Node node) {
            return (node.isLeaf()) ? ImmutableList.of() : ImmutableList.of(node.first, node.second);
        }
    }

    public static EliminationSystemGameGenerator generate(List<Player> players) {
        Node root = generateTree(new LinkedList<>(players));
        PlayerTraverser playerTraverser = new PlayerTraverser();
        FluentIterable iterable = playerTraverser.postOrderTraversal(root);
        return new EliminationSystemGameGenerator(iterable.iterator());
    }

    private static Node generateTree(List<Player> players) {
        int size = players.size();
        if(size == 1) {
            return new Node(players.get(0));
        } else if (size > 1) {
            Collections.shuffle(players);
            List<Player> leftPlayers = players.subList(0, players.size() / 2);
            List<Player> rightPlayers = players.subList(players.size() / 2, players.size());
            Node leftNode = generateTree(leftPlayers);
            Node rightNode = generateTree(rightPlayers);
            return new Node(leftNode, rightNode);
        } else {
            return null;
        }
    }

    private EliminationSystemGameGenerator(Iterator it) {
        this.it = it;
    }

    public void prepareNext() {
        while(node == null && it.hasNext()) {
            node = (Node) it.next();
            if(node.getPlayer() == null) {
                break;
            } else {
                node = null;
            }
        }
    }

    @Override
    public boolean hasNext() {
        prepareNext();
        return node != null;
    }

    @Override
    public Game generateNext() {
        Backgammon backgammon = new Backgammon();
        backgammon.initialize();
        prepareNext();
        if(node != null) {
            Game game = new Game(node.getFirst().getPlayer(), node.getSecond().getPlayer());
            if(game != null) {
                try {
                    game.play(backgammon);
                    node.setPlayer(game.getWinner());
                    node = null;
                } catch (Exception ex) {
                    node = null;
                    return null;
                }
            } else {
                node = null;
                return null;
            }
            return game;
        } else {
            return null;
        }
    }
}

