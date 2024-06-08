package movement;

import board.Board;
import board.BoardUtil;

import java.util.*;
import java.util.stream.DoubleStream;

public class MoveTreeService {

    private MoveTree moveTree = new MoveTree();

    private final int searchDepthLimit;

    public MoveTreeService(int searchDepthLimit) {
        this.searchDepthLimit = searchDepthLimit;
    }

    public Move resolveBestMove(boolean isBotWhite, boolean isBotToMove, Board board) {
        initialiseScores(moveTree.getNodes(), isBotWhite, isBotToMove);

        return resolveBestMoveFromScores(isBotToMove);
        //update movetree before returning then all we need to do is update once the player has made their move
    }

    private Move resolveBestMoveFromScores(boolean isBotToMove) {
        Move move;
        if (isBotToMove) {
            move = moveTree.getNodes()
                    .stream()
                    .max(Comparator.comparingDouble(MoveNode::getScore))
                    .orElseThrow()
                    .getMove();
        } else {
            move = moveTree.getNodes()
                    .stream()
                    .min(Comparator.comparingDouble(MoveNode::getScore))
                    .orElseThrow()
                    .getMove();
        }
        return move;
    }

    private List<Double> initialiseScores(List<MoveNode> nodes, boolean isBotWhite, boolean isBotToMove) {
        List<Double> nodeScores = new ArrayList<>();
        for (MoveNode node : nodes) {
            if (!node.hasChildren()) {
                for (MoveNode childNode : nodes) {
                    double score = BoardUtil.getScoreForBoardState(childNode.getBoardSnapshot(), isWhiteToMove(isBotWhite, isBotToMove));
                    childNode.setScore(score);
                    nodeScores.add(score);
                }
            } else {
                List<Double> childScores = initialiseScores(node.getChildren(), isBotWhite, isBotToMove);

                DoubleStream childScoresDoubleStream = childScores.stream().mapToDouble(Double::doubleValue);
                OptionalDouble score = isBotToMove ? childScoresDoubleStream.max() : childScoresDoubleStream.min();

                node.setScore(score.orElseThrow());
                nodeScores.add(score.orElseThrow());
            }
        }

        return nodeScores;
    }

    private boolean isWhiteToMove(boolean isBotWhite, boolean isBotToMove) {
        return isBotWhite == isBotToMove;
    }

    public void constructMoveTree(boolean isWhiteToMove, boolean isFriendlyToMove, Board board) {
        moveTree = new MoveTree();
        initialiseMoveTree(isWhiteToMove, board);
        createMoveLayers(0, !isWhiteToMove, !isFriendlyToMove, board, moveTree.getNodes());
    }

    //TODO We need to do something differently if we are in checkmate
    private int createMoveLayers(int searchDepth, boolean isWhiteToMove, boolean isBotToMove, Board board, List<MoveNode> moveNodes) {
        if (searchDepth > searchDepthLimit) {
            return --searchDepth;
        }

        for (MoveNode moveNode : moveNodes) {
            //Update a copy of the existing board with this move, so we can create the next possible moves
            Board boardInstanceForMove = moveNode.getBoardSnapshot();

            Set<Integer> playersPieceLocations = boardInstanceForMove.getFriendlyPieceLocations(isWhiteToMove);

            playersPieceLocations.forEach(index -> boardInstanceForMove.getPieceByIndex(index)
                    .getPossibleMoves(index, boardInstanceForMove)
                    .forEach(move -> {
                        Board boardInstanceForNewMove = new Board(boardInstanceForMove);
                        boardInstanceForNewMove.updateBoard(move, isWhiteToMove);
                        moveNode.getChildren()
                                .add(new MoveNode(move, moveNode, boardInstanceForNewMove));
                    }));
            searchDepth = createMoveLayers(++searchDepth, !isWhiteToMove, !isBotToMove, boardInstanceForMove, moveNode.getChildren());
        }

        return searchDepth;
    }

    private void initialiseMoveTree(boolean isWhiteToMove, Board board) {
        for (Integer index : board.getFriendlyPieceLocations(isWhiteToMove)) {
            createInitialMoveNodesFromPieceLocation(board, index, isWhiteToMove);
        }
    }

    private void createInitialMoveNodesFromPieceLocation(Board board, Integer index, boolean isWhiteToMove) {
        board.getPieceByIndex(index).getPossibleMoves(index, board)
                .forEach(move -> {
                    Board boardInstanceForNewMove = new Board(board);
                    boardInstanceForNewMove.updateBoard(move, isWhiteToMove);
                    moveTree.getNodes()
                            .add(new MoveNode(move, boardInstanceForNewMove));
                });
    }

    public MoveTree getMoveTree() {
        return moveTree;
    }
}
