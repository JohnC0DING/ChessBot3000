package movement;

import board.Board;
import board.BoardUtil;

import java.util.*;
import java.util.stream.IntStream;

public class MoveTreeService {

    MoveTree moveTree;

    private static final int SEARCH_DEPTH_LIMIT = 4;

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
                    .max(Comparator.comparingInt(MoveNode::getScore))
                    .orElseThrow()
                    .getMove();
        } else {
            move = moveTree.getNodes()
                    .stream()
                    .min(Comparator.comparingInt(MoveNode::getScore))
                    .orElseThrow()
                    .getMove();
        }
        return move;
    }

    private List<Integer> initialiseScores(List<MoveNode> nodes, boolean isBotWhite, boolean isBotToMove) {
        List<Integer> nodeScores = new ArrayList<>();

        for (MoveNode node : nodes) {
            if (!node.hasChildren()) {
                for (MoveNode childNode : nodes) {
                    int score = BoardUtil.getScoreForBoardState(childNode.getBoardSnapshot(), isBotWhite);
                    childNode.setScore(score);
                    nodeScores.add(score);
                }
            } else {
                List<Integer> childScores = initialiseScores(node.getChildren(), isBotWhite, isBotToMove);

                IntStream childScoresIntStream = childScores.stream().mapToInt(Integer::intValue);
                OptionalInt score = isBotToMove ? childScoresIntStream.max() : childScoresIntStream.min();

                node.setScore(score.orElseThrow());
                nodeScores.add(score.orElseThrow());
            }
        }

        return nodeScores;
    }

    public MoveTree constructMoveTree(int searchDepth, boolean isWhiteToMove, boolean isBotToMove, Board board) {
        moveTree = new MoveTree();

        List<MoveNode> moveNodes = new ArrayList<>();

        if (moveTree.getNodes().isEmpty()) {
            initialiseMoveTree(isWhiteToMove, isBotToMove, board);
            moveNodes = moveTree.getNodes();
        }

        createMoveLayers(searchDepth, !isWhiteToMove, !isBotToMove, board, moveNodes);

        return moveTree;
    }

    //TODO We need to do something differently if we are in checkmate
    private int createMoveLayers(int searchDepth, boolean isWhiteToMove, boolean isBotToMove, Board board, List<MoveNode> moveNodes) {
        if (searchDepth > SEARCH_DEPTH_LIMIT) {
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

    private void initialiseMoveTree(boolean isWhiteToMove, boolean isBotToMove, Board board) {
        if (isBotToMove) {
            for (Integer index : board.getFriendlyPieceLocations(isWhiteToMove)) {
                createInitialMoveNodesFromPieceLocation(board, index, isWhiteToMove);
            }
        } else {
            for (Integer index : board.getOpponentPieceLocations(isWhiteToMove)) {
                createInitialMoveNodesFromPieceLocation(board, index, isWhiteToMove);
            }
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
}
