package player;

import board.Board;
import movement.Move;
import util.Pair;

import java.util.HashMap;
import java.util.Map;

public class Bot {


    private boolean isWhite;

    public Bot(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public Pair<String, Move> makeMove(Board board) {
        //Construct tree of possible moves
        //for now create one level of the tree then pick at random
        //Evaluate moves
        //Convert to fen

        Map<Pair<Integer, Integer>, Integer>  movesToScores = new HashMap<>();

        boolean isBotMove = true;
        for(int i = 0; i < 2; i++) {
            if(isBotMove){
                for (Integer index : board.getFriendlyPieceLocations(isWhite)) {
                    board.getPieceByIndex(index)
                            .ifPresent(piece ->
                                    movesToScores.putAll(piece.getPossibleMoves(index, board)));
                }
            } else {
                    for (Integer index : board.getOpponentPieceLocations(isWhite)) {
                        board.getPieceByIndex(index)
                                .ifPresent(piece ->
                                        movesToScores.putAll(piece.getPossibleMoves(index, board)));
                    }
            }
            isBotMove = !isBotMove;
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
