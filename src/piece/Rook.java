package piece;

import board.Board;

import java.util.*;

public class Rook implements Piece{

    private boolean isWhite;

    private boolean isFriendly;

    public Rook(boolean isWhite, boolean isFriendly) {
        this.isWhite = isWhite;
        this.isFriendly = isFriendly;
    }

    @Override
    public Map<Integer, Integer> getPossibleMoves(int position, Board board) {
        return MoveUtil.getPossibleMovesForSlidingPiece(position, board, SlidingType.STRAIGHT);
    }

    @Override
    public boolean isFriendly() {
        return isFriendly;
    }

    @Override
    public boolean isWhite() {
        return isWhite;
    }

    @Override
    public int getScore() {
        return 5;
    }
}
