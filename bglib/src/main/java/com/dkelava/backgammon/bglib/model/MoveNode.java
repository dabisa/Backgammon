package com.dkelava.backgammon.bglib.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.TreeTraverser;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * MoveNode represents a tree of all possible moves for some board state.
 * This class is immutable.
 */
public final class MoveNode {

    private final Node<Move> _node;

    private MoveNode(Node<Move> moves) {
        _node = moves;
    }

    /**
     * Generates MoveNode for given board state, current player color and available dice
     *
     * @param board Board state
     * @param color Current player's color
     * @param dice Available dice
     * @return Generated MoveNode
     */
    public static MoveNode generate(Board board, Color color, DiceSet dice) {
        Node<Move> root = new Node<>();
        generateRecursive(board, color, dice, root);

        int maxDepth = calculateMaximumDepth(root);
        filterDepth(root, maxDepth);

        int maxDistance = calculateMaximumDistance(root);
        filterDistance(root, maxDistance);

        removeAlternatives(root);
        return new MoveNode(root);
    }

    private static void generateRecursive(Board board, Color color, DiceSet dice, Node<Move> node) {
        for (Point point : Point.getPoints(color)) {
            for (Die.Face die : dice.getDice()) {
                if (Move.isMovable(board, color, point, die)) {
                    Move move = Move.createMove(board, color, point, die);
                    Node<Move> child = node.addChild(new Node(move));
                    if (dice.getTotalMoves() > 1) {
                        move.execute(board);
                        dice.remove(die);
                        generateRecursive(board, color, dice, child);
                        dice.add(die);
                        move.rollback(board);
                    }
                }
            }
        }
    }

    private static int calculateDistance(Node<Move> node) {
        int distance = 0;
        Node<Move> n = node;
        while (n != null && n.getParent() != null) {
            Move move = n.getData();
            distance += move.getDistance();
            n = n.getParent();
        }
        return distance;
    }

    private static int calculateDepth(Node<Move> node) {
        int depth = 0;
        Node<Move> n = node;
        while (n != null && n.getParent() != null) {
            depth++;
            n = n.getParent();
        }
        return depth;
    }

    private static int calculateDiceUsed(Node<Move> node) {
        int dice = 0;
        Node<Move> n = node;
        while (n != null && n.getParent() != null) {
            Move move = n.getData();
            dice += move.getDie().getValue();
            n = n.getParent();
        }
        return dice;
    }

    private static int calculateMaximumDistance(Node<Move> root) {
        int maxDistance = 0;
        Iterator<Node<Move>> it = new MoveTraverser().postOrderTraversal(root).iterator();
        while (it.hasNext()) {
            Node<Move> node = it.next();
            int distance = calculateDistance(node);
            if (distance > maxDistance) {
                maxDistance = distance;
            }
        }
        return maxDistance;
    }

    private static int calculateMaximumDepth(Node<Move> root) {
        int maxDepth = 0;
        Iterator<Node<Move>> it = new MoveTraverser().postOrderTraversal(root).iterator();
        while (it.hasNext()) {
            Node<Move> node = it.next();
            int depth = calculateDepth(node);
            if (depth > maxDepth) {
                maxDepth = depth;
            }
        }
        return maxDepth;
    }

    private static void filterDepth(Node<Move> root, int maxDepth) {
        while (true) {
            Set<Node<Move>> killList = new HashSet<>();

            Iterator<Node<Move>> it = new MoveTraverser().postOrderTraversal(root).iterator();
            while (it.hasNext()) {
                Node<Move> node = it.next();
                int depth = calculateDepth(node);
                if (!node.hasChildren() && depth < maxDepth) {
                    killList.add(node);
                }
            }

            if (!killList.isEmpty()) {
                for (Node<Move> leaf : killList) {
                    leaf.detach();
                }
            } else {
                break;
            }
        }
    }

    private static void filterDistance(Node<Move> root, int maxDistance) {
        while (true) {
            Set<Node<Move>> killList = new HashSet<>();

            Iterator<Node<Move>> it = new MoveTraverser().postOrderTraversal(root).iterator();
            while (it.hasNext()) {
                Node<Move> node = it.next();
                int distance = calculateDistance(node);
                if (!node.hasChildren() && distance < maxDistance) {
                    killList.add(node);
                }
            }

            if (!killList.isEmpty()) {
                for (Node<Move> leaf : killList) {
                    leaf.detach();
                }
            } else {
                break;
            }
        }
    }

    private static void removeAlternatives(Node<Move> root) {
        List<Node<Move>> children = ImmutableList.copyOf(root.getChildren());
        for (Node<Move> node : children) {
            for (Node<Move> siblingNode : children) {
                if (siblingNode != node) {
                    Move move = node.getData();
                    Move siblingMove = siblingNode.getData();
                    if (move.getWaste() > siblingMove.getWaste() && move.getSource() == siblingMove.getSource() && move.getDestination() == siblingMove.getDestination()) {
                        node.detach();
                        return;
                    }
                }
            }
        }
    }

    private static void visitNode(Node<Move> node, Visitor visitor) {
        Move move = node.getData();
        if (move != null) {
            visitor.enter(move);
        }
        for (Node<Move> child : node.children) {
            visitNode(child, visitor);
        }
        if (move != null) {
            visitor.exit(move);
        }
    }

    /**
     * Returns available dice
     */
    public final DiceSet getDiceState() {
        DiceSet diceSet = DiceSet.createEmpty();
        Node<Move> node = _node;
        while (node.hasChildren()) {
            node = node.getChild(0);
            diceSet.add(node.getData().getDie());
        }
        return diceSet;
    }

    /**
     * Determines if checker on specified point is movable
     *
     * @returns true Checker on specified point is movable
     * @returns false There is no movable checker on specified point
     */
    public final boolean isMovable(Point source) {
        for (Node<Move> node : _node.getChildren()) {
            Move move = node.getData();
            if (move.getSource() == source) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if checker on specified point can be moved to specified destination
     *
     * @returns true Specified move is possible
     * @returns false Specified move is not possible
     */
    public final boolean isMovable(Point source, Point destination) {
        return source != destination && findNode(source, destination) != null;
    }

    /**
     * Determines if moving from specified source point to specified destination point hit opponent checker.
     */
    public final boolean isHit(Point source, Point destination) {
        if (source != destination) {
            Node<Move> node = findNode(source, destination);
            if (node == null) {
                return false;
            }
            boolean isHit = false;
            while (node != _node) {
                if (node.getData() != null && node.getData().isHit()) {
                    isHit = true;
                }
                node = node.getParent();
            }
            return isHit;
        } else {
            return false;
        }
    }

    /**
     * Determines if MoveNode represents beginning of a players turn
     */
    public final boolean isStart() {
        return _node.getParent() == null;
    }

    /**
     * Determines if MoveNode represents end of players turn.
     */
    public boolean isEnd() {
        return !_node.hasChildren();
    }

    /**
     * Find node in this tree that corresponds to move from source point to destination point.
     */
    public final MoveNode find(Point source, Point destination) {
        Node<Move> last = findNode(source, destination);
        return (last != null) ? new MoveNode(last) : null;
    }

    /**
     * Get parent node that corresponds to MoveNode before previous move.
     */
    public final MoveNode getPrevious() {
        if (!isStart()) {
            return new MoveNode(_node.getParent());
        } else {
            return null;
        }
    }

    /**
     * Get moves that correspond to difference between this node and given previous node
     */
    public List<Move> getMoves(MoveNode startingPosition) {
        if (isParent(startingPosition._node)) {
            List<Move> moves = new LinkedList<>();
            Node<Move> node = _node;
            while (node != startingPosition._node) {
                moves.add(0, node.getData());
                node = node.getParent();
            }
            return moves;
        } else {
            return null;
        }
    }

    /**
     * Retrieves root node of the move tree.
     */
    public MoveNode getRoot() {
        MoveNode node = this;
        while (!node.isStart()) {
            node = node.getPrevious();
        }
        return node;
    }

    /**
     * Retrieves move that corresponds to this node.
     */
    public Move getMove() {
        return _node.getData();
    }

    private boolean isParent(Node<Move> parent) {
        Node<Move> node = _node;
        while (node != null) {
            node = node.getParent();
            if (node == parent) return true;
        }
        return false;
    }

    private Node<Move> findNode(Point source, Point destination) {
        for (Node<Move> node : _node.getChildren()) {
            if (node.getData().getSource() == source) {
                if (node.getData().getDestination() == destination) {
                    return node;
                } else if (!node.getData().isHit()) {
                    Node<Move> node2 = new MoveNode(node).findNode(node.getData().getDestination(), destination);
                    if (node2 != null) {
                        return node2;
                    }
                }
            }
        }

        return null;
    }

    /**
     * Visits all nodes recursively, calling enter method on visitor when entering subtree
     * and calling exit method on visitor when exiting subtree
     *
     * For each node:
     *  1) Call enter method
     *  2) Visit childeren
     *  3) Call exit method
     */
    public void visit(Visitor visitor) {
        for (Node<Move> child : _node.children) {
            visitNode(child, visitor);
        }
    }

    public interface Visitor {
        void enter(Move move);
        void exit(Move move);
    }

    private static class Node<T> {
        private T data = null;
        private Node<T> parent;
        private List<Node<T>> children = new LinkedList<>();

        public Node() {
        }

        public Node(T data) {
            this.data = data;
        }

        Node<T> getParent() {
            return parent;
        }

        Node<T> getChild(int i) {
            return children.get(i);
        }

        T getData() {
            return data;
        }

        List<Node<T>> getChildren() {
            return children;
        }

        boolean hasChildren() {
            return !children.isEmpty();
        }

        Node<T> addChild(Node<T> node) {
            children.add(node);
            node.parent = this;
            return node;
        }

        void detach() {
            if (parent != null) {
                parent.children.remove(this);
                parent = null;
            } else {
                throw new RuntimeException("detaching root node");
            }
        }
    }

    private static class NodeTraverser<T> extends TreeTraverser<Node<T>> {
        @Override
        public Iterable<Node<T>> children(Node<T> root) {
            return root.children;
        }
    }

    private static class MoveTraverser extends NodeTraverser<Move> {
    }
}
