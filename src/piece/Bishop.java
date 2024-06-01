package piece;

import board.Board;
import movement.Move;
import movement.PieceMoveUtil;
import util.Pair;

import java.util.List;
import java.util.Map;

public class Bishop extends Piece {

    public Bishop(boolean isWhite, boolean isFriendly) {
        super(isWhite, isFriendly);
    }

    @Override
    public List<Move> getPossibleMoves(int index, Board board) {
        return PieceMoveUtil.getPossibleMovesForSlidingPiece(index, board, SlidingType.DIAGONAL);
    }

    @Override
    public boolean canSeeOpponentPiece(int currentIndex, int opponentIndex, Board board) {
        return PieceMoveUtil.canSeeIndexForSlidingPiece(currentIndex, opponentIndex, board, SlidingType.DIAGONAL);
    }

    @Override
    public int getScore() {
        return 3;
    }

    @Override
    public Pieces getCharacterRepresentation(){
        if(isWhite()){
            return Pieces.B;
        }
        return Pieces.b;
    }
}
