package piece;

import board.Board;
import movement.Move;
import movement.PieceMoveUtil;
import util.Pair;

import java.util.List;
import java.util.Map;

public class Queen extends Piece {

    public Queen(boolean isWhite, boolean isFriendly) {
        super(isWhite, isFriendly);
    }

    @Override
    public List<Move> getPossibleMoves(int index, Board board) {
        return PieceMoveUtil.getPossibleMovesForSlidingPiece(index, board, SlidingType.BOTH);
    }

    @Override
    public boolean canSeeOpponentPiece(int currentIndex, int opponentIndex, Board board) {
        return PieceMoveUtil.canSeeIndexForSlidingPiece(currentIndex, opponentIndex, board, SlidingType.BOTH);
    }

    @Override
    public int getScore() {
        return 9;
    }

    @Override
    public Pieces getCharacterRepresentation(){
        if(isWhite()){
            return Pieces.Q;
        }
        return Pieces.q;
    }

}
