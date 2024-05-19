package piece;

import board.Board;

import java.util.Map;

public class Queen implements Piece{

    private boolean isWhite;

    private boolean isFriendly;

    public Queen(boolean isWhite, boolean isFriendly) {
        this.isWhite = isWhite;
        this.isFriendly = isFriendly;
    }

    @Override
    public Map<Integer, Integer> getPossibleMoves(int position, Board board) {
        return MoveUtil.getPossibleMovesForSlidingPiece(position, board, SlidingType.BOTH);
    }

    @Override
    public boolean isFriendly() {
        return isFriendly;
    }

    @Override
    public int getScore() {
        return 9;
    }

    @Override
    public boolean isWhite() {
        return isWhite;
    }
}
