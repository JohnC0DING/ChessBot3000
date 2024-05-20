package piece;

import board.Board;
import util.Pair;

import java.util.Map;

public class Bishop implements Piece {

    private boolean isWhite;

    private boolean isFriendly;

    public Bishop(boolean isWhite, boolean isFriendly) {
        this.isWhite = isWhite;
        this.isFriendly = isFriendly;
    }

    @Override
    public Map<Pair<Integer, Integer>, Integer> getPossibleMoves(int index, Board board) {
        return MoveUtil.getPossibleMovesForSlidingPiece(index, board, SlidingType.DIAGONAL);
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
