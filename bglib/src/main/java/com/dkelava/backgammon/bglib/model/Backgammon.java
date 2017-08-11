package com.dkelava.backgammon.bglib.model;

import com.google.common.collect.ImmutableList;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class represents Backgammon game.
 *
 *  Backgammon state includes:
 *  - game status
 *  - board state
 *  - dice state
 *  - possible moves
 *  - current player
 *  - owner of the cube
 *  - current stake
 *  - winner
 *
 * @note This class is mutable!
 */
public class Backgammon {

    static Die.Strategy defaultStrategy = new RandomDieStrategy();

    private Status status = Status.Empty;
    private Board board = new Board();
    private Color currentPlayer;
    private Color cubeOwner;
    private Color winner;
    private Die dieOne = new Die(defaultStrategy);
    private Die dieTwo = new Die(defaultStrategy);
    private int stake;
    private MoveNode moves;

    private BackgammonState state = new BackgammonState() {
        @Override
        public Status getStatus() {
            return status;
        }

        @Override
        public Color getCurrentPlayer() {
            return currentPlayer;
        }

        @Override
        public Color getCubeOwner() {
            return cubeOwner;
        }

        @Override
        public Color getWinner() {
            return winner;
        }

        @Override
        public PointState getPointState(Point point) {
            return board.getPoint(point);
        }

        @Override
        public Die.Face getDieOne() {
            return dieOne.getFace();
        }

        @Override
        public Die.Face getDieTwo() {
            return dieTwo.getFace();
        }

        @Override
        public MoveNode getMoves() {
            // TODO: 17.2.2017. check if i should be lazy here
            return moves;
        }

        @Override
        public int getStake() {
            return stake;
        }

        @Override
        public boolean isGammon() {
            return board.isGammon();
        }
    };

    private void generateMoves() {
        moves = MoveNode.generate(board, currentPlayer, DiceSet.createRoll(this.dieOne.getFace(), this.dieTwo.getFace()));
        if(moves.isEnd()) {
            status = Status.NoMoves;
        } else {
            status = Status.Moving;
        }
    }

    private Backgammon(Builder builder){
        status = builder.status;
        board = new Board(builder.board);
        currentPlayer = builder.currentPlayer;
        cubeOwner = builder.cubeOwner;
        winner = builder.winner;
        dieOne.setFace(builder.dieOne);
        dieTwo.setFace(builder.dieTwo);
        dieOne.setStrategy(builder.dieOneStrategy);
        dieTwo.setStrategy(builder.dieTwoStrategy);
        stake = builder.stake;
        if(builder.diceSet != null) {
            moves = MoveNode.generate(board, currentPlayer, builder.diceSet);
        } else {
            moves = null;
        }
    }

    /**
     * Default constructor creates Backgammon in initial state
     */
    public Backgammon() {
        initialize();
    }

    public Backgammon(BackgammonState original) {
        restore(original);
    }

    /**
     * Restores backgammon state immutable BackgammonState object
     */
    public void restore(BackgammonState original) {
        for (Point point : Point.values()) {
            board.setPoint(point, original.getPointState(point));
        }
        this.status = original.getStatus();
        this.currentPlayer = original.getCurrentPlayer();
        this.cubeOwner = original.getCubeOwner();
        this.winner = original.getWinner();
        this.dieOne.setFace(original.getDieOne());
        this.dieTwo.setFace(original.getDieTwo());
        this.moves = original.getMoves();
        this.stake = original.getStake();
    }

    /**
     * Restores backgammon state from a string
     */
    public void restore(String gameState) throws Exception {

        Pattern pattern = Pattern.compile("(.)(.)(.)(.)(.)(.)(.{4})(.{84})(.*)");
        Matcher matcher = pattern.matcher(gameState);

        if( matcher.find() ) {
            this.status = Status.decode(matcher.group(1));
            this.currentPlayer = Color.decode(matcher.group(2));
            this.cubeOwner = Color.decode(matcher.group(3));
            this.winner = Color.decode(matcher.group(4));
            this.dieOne.setFace(Die.Face.decode(matcher.group(5)));
            this.dieTwo.setFace(Die.Face.decode(matcher.group(6)));
            this.stake = Integer.parseInt(matcher.group(7));
            this.board.restore(matcher.group(8));

            if(status == Status.Moving || status == Status.NoMoves || status == Status.End) {
                String g = matcher.group(9);
                Pattern pattern2 = Pattern.compile(".....");
                Matcher matcher2 = pattern2.matcher(g);

                List<Move> executedMoves = new LinkedList<>();
                while (matcher2.find()) {
                    String moveString = matcher2.group();
                    Move move = Move.decode(moveString);
                    executedMoves.add(move);
                }

                for (Move move : ImmutableList.copyOf(executedMoves).reverse()) {
                    if (move.canRollback(board)) {
                        move.rollback(board);
                    } else {
                        throw new IllegalArgumentException("invalid game state" + gameState);
                    }
                }
                generateMoves();
                for (Move move : executedMoves) {
                    move(move.getSource(), move.getDestination());
                }
            }
        } else {
            throw new IllegalArgumentException("invalid game state: " + gameState);
        }
    }

    /**
     * Encode backgammon state in a string
     */
    public String encode() {
        StringBuilder builder = new StringBuilder();
        builder.append(status.encode());
        builder.append(currentPlayer.encode());
        builder.append(cubeOwner.encode());
        builder.append(winner.encode());
        builder.append(dieOne.getFace().encode());
        builder.append(dieTwo.getFace().encode());
        builder.append(String.format("%04d", stake));
        builder.append(board.encode());

        if(moves != null) {
            MoveNode root = moves.getRoot();
            List<Move> executedMoves = moves.getMoves(root);
            if(executedMoves != null) {
                for (Move move : executedMoves) {
                    builder.append(move.encode());
                }
            }
        }

        return builder.toString();
    }

    /**
     * Retrieves immutable backgammon state
     */
    public BackgammonState getState() {
        return state;
    }

    /**
     * Sets initial backgammon state
     */
    public void initialize(){
        status = Status.Initial;
        board.initialize();
        currentPlayer = Color.White;
        cubeOwner = Color.None;
        winner = Color.None;
        dieOne.clear();
        dieTwo.clear();
        stake = 1;
        moves = null;
    }

    /**
     * Simulates dice roll by setting dice faces to specified values.
     * If provided values are null, dice rolling strategy is used to generate die values.
     */
    public void roll(Die.Face dieOne, Die.Face dieTwo) {
        switch (status) {
            case Initial:
            case Rolling:
                dieOne = this.dieOne.roll(dieOne);
                dieTwo = this.dieTwo.roll(dieTwo);

                switch(status) {
                    case Initial:
                        if(dieOne.getValue() > dieTwo.getValue()) {
                            currentPlayer = Color.White;
                            generateMoves();
                        } else if(dieOne.getValue() < dieTwo.getValue()) {
                            currentPlayer = Color.Black;
                            generateMoves();
                        }
                        break;

                    case Rolling:
                        generateMoves();
                        break;
                }
                break;

            default:
                throw new IllegalStateException("roll: " + this.encode());
        }
    }

    /**
     * Undoes initial roll
     *
     * @param prevDie Die face value of the previous state (null if no previous value)
     */
    public void undoInitialRoll(Die.Face prevDie) {
        switch (status) {
            case Initial:
            case Moving:
            case NoMoves:
                this.dieOne.setFace(prevDie);
                this.dieTwo.setFace(prevDie);
                moves = null;
                status = Status.Initial;
                currentPlayer = Color.White;
                break;

            default:
                throw new IllegalStateException("undo initial roll: " + this.encode());
        }
    }

    /**
     * Undoes roll
     */
    public void undoRoll() {
        switch (status) {
            case Moving:
            case NoMoves:
                if(moves.isStart()) {
                    this.dieOne.clear();
                    this.dieTwo.clear();
                    moves = null;
                    status = Status.Rolling;
                } else {
                    throw new IllegalStateException("undo roll: " + this.encode());
                }
                break;

            default:
                throw new IllegalStateException("undo roll: " + this.encode());
        }
    }

    /**
     * Clear dice ater no more moves are possible.
     */
    public void clearDice() {
        switch (status) {
            case NoMoves:
                currentPlayer = currentPlayer.getOpponent();
                dieOne.clear();
                dieTwo.clear();
                moves = null;
                status = Status.Rolling;
                break;

            default:
                throw new IllegalStateException("clear dice" + this.encode());
        }
    }

    /**
     * Undoes clear dice action
     *
     * @param dieOne First die value in the previous turn
     * @param dieTwo Second die value in the previous turn
     * @param moves List of moves that were done in the previous turn
     */
    public void undoClearDice(Die.Face dieOne, Die.Face dieTwo, List<Move> moves) {
        switch (status) {
            case Rolling:
                currentPlayer = currentPlayer.getOpponent();
                DiceSet diceSet = DiceSet.createEmpty();
                for (Move move : ImmutableList.copyOf(moves).reverse()) {
                    if (move.canRollback(board)) {
                        move.rollback(board);
                    } else {
                        throw new IllegalStateException("undo clear dice: " + this.encode());
                    }
                    diceSet.add(move.getDie());
                }

                this.status = Status.NoMoves;
                this.dieOne.setFace(dieOne);
                this.dieTwo.setFace(dieTwo);

                this.moves = MoveNode.generate(board, currentPlayer, diceSet);
                DiceSet genDiceSet =this.moves.getDiceState();
                if (!genDiceSet.equals(diceSet)) {
                    throw new IllegalStateException("undo clear dice: " + this.encode());
                }

                for (Move move : moves) {
                    if (move.canExecute(board)) {
                        this.moves = this.moves.find(move.getSource(), move.getDestination());
                        move.execute(board);
                    } else {
                        throw new IllegalStateException("undo clear dice: " + this.encode());
                    }
                }
                break;

            default:
                throw new IllegalStateException("undo clear dice: " + this.encode());
        }
    }

    /**
     * Offer double stake to opponent.
     */
    public void doubleStake() {
        switch(status) {
            case Rolling:
                if(cubeOwner == Color.None || cubeOwner == currentPlayer) {
                    currentPlayer = currentPlayer.getOpponent();
                    status = Status.DoubleStake;
                }
                break;

            default:
                throw new IllegalStateException("double stake: " + this.encode());
        }
    }

    /**
     * Undoes double stake offer
     */
    public void undoDoubleStake() {
        switch(status) {
            case DoubleStake:
                currentPlayer = currentPlayer.getOpponent();
                status = Status.Rolling;
                break;

            default:
                throw new IllegalStateException("undo double stake: " + this.encode());
        }
    }

    /**
     * Accept double stake
     */
    public void acceptDouble() {
        switch(status) {
            case DoubleStake:
                stake *= 2;
                cubeOwner = currentPlayer;
                currentPlayer = currentPlayer.getOpponent();
                status = Status.Rolling;
                break;

            default:
                throw new IllegalStateException("accept double: " + this.encode());
        }
    }

    /**
     * Undoes double stake accept
     */
    public void undoAcceptDouble() {
        switch(status) {
            case Rolling:
                stake /= 2;
                if(stake == 1) {
                    cubeOwner = Color.None;
                } else {
                    cubeOwner = cubeOwner.getOpponent();
                }
                currentPlayer = currentPlayer.getOpponent();
                status = Status.DoubleStake;
                break;

            default:
                throw new IllegalStateException("undo accept double: " + this.encode());
        }
    }

    /**
     * Rejects double stake
     */
    public void rejectDouble() {
        switch (status) {
            case DoubleStake:
                winner = currentPlayer.getOpponent();
                status = Status.Resigned;
                break;

            default:
                throw new IllegalStateException("reject double: " + this.encode());
        }
    }

    /**
     * Undoes double stake reject
     */
    public void undoRejectDouble() {
        switch (status) {
            case Resigned:
                status = Status.DoubleStake;
                winner = Color.None;
                break;

            default:
                throw new IllegalStateException("undo reject double: " + this.encode());
        }
    }

    /**
     * Moves checker on board
     *
     * @param source Source point of moving checker
     * @param destination Destination point of moving checker
     */
    public void move(Point source, Point destination) {
        switch (status) {
            case Moving:
                if(moves.isMovable(source, destination)) {
                    MoveNode target = moves.find(source, destination);
                    for (Move move : target.getMoves(moves)) {
                        move.execute(board);
                    }
                    moves = target;

                    if (board.getPoint(Point.getHome(currentPlayer)).getQuantity() == 15) {
                        status = Status.End;
                        winner = currentPlayer;
                    } else if (moves.isEnd()) {
                        status = Status.NoMoves;
                    }
                } else {
                    throw new IllegalStateException("move from " + source.encode() + " to " + destination.encode() + ": " + this.encode());
                }
                break;

            default:
                throw new IllegalStateException("move: " + this.encode());
        }
    }

    /**
     * Undoes checker move
     *
     * @param source Source point of previously performed move
     * @param destination Destination point of previously performed move
     */
    public void undoMove(Point source, Point destination) {
        switch (status) {
            case Moving:
            case NoMoves:
            case End:
                if(moves != null && !moves.isStart() && moves.getMove().getDestination() == destination) {
                    MoveNode node = moves;
                    while (node != null && node.getMove() != null && node.getMove().getSource() != source) {
                        node = node.getPrevious();
                    }
                    if(node== null || node.getMove() == null) {
                        throw new IllegalStateException("undo move: " + this.encode());
                    }
                    MoveNode parent = node.getPrevious();
                    if (parent != null) {
                        ImmutableList<Move> movesToUndo = ImmutableList.copyOf(moves.getMoves(parent));
                        for (Move move : movesToUndo.reverse()) {
                            move.rollback(board);
                        }
                        moves = parent;
                    }

                    if (status == Status.End) {
                        winner = Color.None;
                    }
                    status = Status.Moving;
                } else {
                    throw new IllegalStateException("undo move: " + this.encode());
                }
                break;

            default:
                throw new IllegalStateException("undo move: " + this.encode());
        }
    }

    /**
     * Backgammon builder
     */
    public static class Builder {

        private Status status;
        private Color currentPlayer;
        private Color cubeOwner;
        private Color winner;
        private Board board = new Board();
        private Die.Face dieOne;
        private Die.Face dieTwo;
        private Die.Strategy dieOneStrategy = defaultStrategy;
        private Die.Strategy dieTwoStrategy = defaultStrategy;

        private DiceSet diceSet;
        private int stake;

        public void setStatus(Status status) {
            this.status = status;
        }

        public void setCurrentPlayer(Color color) {
            currentPlayer = color;
        }

        public void setCubeOwner(Color color) {
            cubeOwner = color;
        }

        public void setWinner(Color color) {
            winner = color;
        }

        public void setPointState(Point point, PointState state) {
            board.setPoint(point, state);
        }

        public void setBoardState(Board board) {
            for (Point point : Point.values()) {
                setPointState(point, board.getPoint(point));
            }
        }

        public void setDieOneStrategy(Die.Strategy strategy) {
            dieOneStrategy = strategy;
        }

        public void setDieTwoStrategy(Die.Strategy strategy) {
            dieTwoStrategy = strategy;
        }

        public void setDiceState(Die.Face dieOne, int movesOne, Die.Face dieTwo, int movesTwo) {
            this.dieOne = dieOne;
            this.dieTwo = dieTwo;
            if(movesOne >= 0 && movesTwo >= 0) {
                diceSet = DiceSet.createCustom(dieOne, movesOne, dieTwo, movesTwo);
            } else {
                diceSet = null;
            }
        }

        public void setStake(int stake) {
            this.stake = stake;
        }

        public boolean isLegal() {
            if (currentPlayer == null) {
                return false;
            }

            if (!board.isLegal()) {
                return false;
            }

            if (status == Status.Moving || status == Status.NoMoves) {
                if( diceSet == null)
                {
                    return false;
                }

                MoveNode tree = MoveNode.generate(board, currentPlayer, diceSet);
                DiceSet generatedDiceSet = tree.getDiceState();

                if( !diceSet.equals(generatedDiceSet) ) {
                    return false;
                }
            }

            if(status == Status.Empty) {
                if (stake != 0) {
                    return false;
                }
            } else if(status == Status.Initial) {
                if (stake != 1) {
                    return false;
                }
            } else {
                if (stake < 1) {
                    return false;
                }
            }

            return true;
        }

        public Backgammon build() {
            if (isLegal()) {
                Backgammon tmp = new Backgammon(this);
                return tmp;
            } else {
                return null;
            }
        }
    }
}
