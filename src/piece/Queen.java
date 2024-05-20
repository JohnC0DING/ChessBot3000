package piece;

import board.Board;
import util.Pair;

import java.util.Map;

public class Queen implements Piece{

    private boolean isWhite;

    private boolean isFriendly;

    public Queen(boolean isWhite, boolean isFriendly) {
        this.isWhite = isWhite;
        this.isFriendly = isFriendly;
    }

    @Override
    public Map<Pair<Integer, Integer>, Integer> getPossibleMoves(int index, Board board) {
        return MoveUtil.getPossibleMovesForSlidingPiece(index, board, SlidingType.BOTH);
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
