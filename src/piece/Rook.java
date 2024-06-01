package piece;

import board.Board;
import movement.Move;
import movement.PieceMoveUtil;
import util.Pair;

import java.util.*;

public class Rook extends Piece {

    public Rook(boolean isWhite, boolean isFriendly) {
        super(isWhite, isFriendly);
    }

    @Override
    public List<Move> getPossibleMoves(int index, Board board) {
        return PieceMoveUtil.getPossibleMovesForSlidingPiece(index, board, SlidingType.STRAIGHT);
    }

    @Override
    public boolean canSeeOpponentPiece(int currentIndex, int opponentIndex, Board board) {
        return PieceMoveUtil.canSeeIndexForSlidingPiece(currentIndex, opponentIndex, board, SlidingType.STRAIGHT);
    }

    @Override
    public int getScore() {
        return 5;
    }

    @Override
    public Pieces getCharacterRepresentation() {
        if (isWhite()) {
            return Pieces.R;
        }
        return Pieces.r;
    }
}
