package movement;

import board.Board;
import board.BoardUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class MoveTreeService {

    MoveTree moveTree;

    private static final int SEARCH_DEPTH_LIMIT = 2;

    public Move resolveBestMove(boolean isBotWhite, boolean isBotToMove, Board board){
        int bestScore = 0;
        Move bestMove = null;

        //Move to bottom nodes
        //Score - done
        //Once reached the end of the current nodes
            //Move up
            //Assign a score
        //Move to next node on current level
            //move down score bottom nodes
            //Once reached the end of the current bottom nodes
                //Move up 
                //If no parent we are done adding scores
                //Else assign a score
        
        for(MoveNode rootNode : moveTree.getNodes()){

            List<MoveNode> nodes = resolveBottomLevelOfNodes(rootNode);

            nodes.forEach(node -> node.setScore(getScoreForNode(node, isBotWhite)));
        }
    }

    private Optional<Integer> getScoreForNode(MoveNode node, boolean isBotWhite) {
        if(node.getBoardSnapshot().isPresent()){
            BoardUtil.getScoreForBoardState(node.getBoardSnapshot().get(), isBotWhite);
        }
    }

    private List<MoveNode> resolveBottomLevelOfNodes(MoveNode node) {
        List<MoveNode> nodes;
        if(!node.hasChildren()){
            nodes = moveTree.getNodes();
        } else {
            //Get the bottom list of nodes
            nodes = node.getChildren();
            while(nodes.get(0).hasChildren()){
                nodes = nodes.get(0).getChildren();
            }
        }

        return nodes;
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

    //We need to do something differently if we are in checkmate
    private void createMoveLayers(int searchDepth, boolean isWhiteToMove, boolean isBotToMove, Board board, List<MoveNode> moveNodes) {
        if (searchDepth > SEARCH_DEPTH_LIMIT) return;

        for (MoveNode moveNode : moveNodes) {
            Board imaginaryBoardForNode = new Board(board);
            imaginaryBoardForNode.updateBoard(moveNode.getMove());
            
            Set<Integer> playersPieceLocations = isBotToMove ? board.getFriendlyPieceLocations(isWhiteToMove) : board.getOpponentPieceLocations(isWhiteToMove);

            for (Integer index : playersPieceLocations) {
                if (searchDepth == SEARCH_DEPTH_LIMIT) {
                    //bottom row of nodes so include the board instance on the MoveNode
                    board.getPieceByIndex(index).getPossibleMoves(index, board)
                            .forEach(scoreAndMove -> moveNode.getChildren()
                                    .add(new MoveNode(scoreAndMove.left(), scoreAndMove.right(), moveNode, imaginaryBoardForNode)));
                }
                board.getPieceByIndex(index).getPossibleMoves(index, board)
                        .forEach(scoreAndMove -> moveNode.getChildren()
                                .add(new MoveNode(scoreAndMove.left(), scoreAndMove.right(), moveNode)));
            }
            searchDepth++;
            createMoveLayers(searchDepth, !isWhiteToMove, !isBotToMove, board, moveNode.getChildren());
        }
    }

    private void initialiseMoveTree(boolean isWhiteToMove, boolean isBotToMove, Board board) {
        if (isBotToMove) {
            for (Integer index : board.getFriendlyPieceLocations(isWhiteToMove)) {
                createInitialMoveNodesFromPieceLocation(board, index);
            }
        } else {
            for (Integer index : board.getOpponentPieceLocations(isWhiteToMove)) {
                createInitialMoveNodesFromPieceLocation(board, index);
            }
        }
    }

    private void createInitialMoveNodesFromPieceLocation(Board board, Integer index) {
        board.getPieceByIndex(index).getPossibleMoves(index, board)
                .forEach(scoreAndMove -> moveTree.getNodes()
                        .add(new MoveNode(scoreAndMove.left(), scoreAndMove.right())));
    }
}
