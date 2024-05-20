package player;

import board.Board;
import piece.Piece;
import piece.Rook;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Bot {

    private static final int SEARCH_DEPTH = 2;

    private boolean isWhite;

    public Bot(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public String makeMove(Board board) {
        //Construct tree of possible moves
        //for now create one level of the tree then pick at random
        //Evaluate moves
        //Convert to fen

        Map<Integer, Map<Integer, Integer>> originToScoredMoves = new HashMap<>();

        for(int i = 0; i < 2; i++) {
//            for (Integer index : board.getFriendlyPieceLocations(isWhite)) {
//                board.getTileByIndex(index)
//                        .ifPresent(piece ->
//                                originToScoredMoves.put(index, piece.getPossibleMoves(index, board)));
//            }
//            for (Integer index : board.getOpponentPieceLocations(isWhite)) {
//                board.getTileByIndex(index)
//                        .ifPresent(piece ->
//                                originToScoredMoves.put(index, piece.getPossibleMoves(index, board)));
//            }
        }
        //Evaluate scores


        String moveSummary = "22>21";
        board.updateBoard(moveSummary);

        return "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1";
    }

    public boolean isWhite() {
        return isWhite;
    }
}
