package piece;

import board.Board;

import java.util.Map;

public class Bishop implements Piece {

    private boolean isWhite;

    private boolean isFriendly;

    public Bishop(boolean isWhite, boolean isFriendly) {
        this.isWhite = isWhite;
        this.isFriendly = isFriendly;
    }

    @Override
    public Map<Integer, Integer> getPossibleMoves(int position, Board board) {
        return MoveUtil.getPossibleMovesForSlidingPiece(position, board, SlidingType.DIAGONAL);
    }

    @Override
    public boolean isFriendly() {
        return false;
    }

    @Override
    public boolean isWhite() {
        return isWhite;
    }

    @Override
    public int getScore() {
        return 0;
    }
}
