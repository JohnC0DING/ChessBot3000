package piece;

import board.Board;
import util.Pair;

import java.util.Map;

public class Pawn implements Piece{

    private boolean isQueened = false;

    private boolean isWhite;

    private boolean isFriendly;

    public Pawn(boolean isWhite, boolean isFriendly) {
        this.isWhite = isWhite;
        this.isFriendly = isFriendly;
    }

    @Override
    public Map<Pair<Integer, Integer>, Integer> getPossibleMoves(int index, Board board) {
        return MoveUtil.getPossibleMovesForPawn(index, board, isWhite);
    }

    @Override
    public boolean isFriendly() {
        return false;
    }

    @Override
    public int getScore() {
        return 1;
    }

    public void setQueened(boolean queened) {
        isQueened = queened;
    }

    public boolean isQueened() {
        return isQueened;
    }

    @Override
    public boolean isWhite() {
        return isWhite;
    }
}
