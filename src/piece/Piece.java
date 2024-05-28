package piece;

import board.Board;
import movement.Move;
import util.Pair;

import java.util.List;
import java.util.Map;

public abstract class Piece {

    private boolean isWhite;

    private boolean isFriendly;

    public Piece(boolean isWhite, boolean isFriendly) {
        this.isWhite = isWhite;
        this.isFriendly = isFriendly;
    }

    public abstract List<Pair<Integer, Move>> getPossibleMoves(int index, Board board);

    public abstract boolean canSeeOpponentPiece(int opponentIndex, Board board);

    public boolean isFriendly() {
        return false;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public abstract int getScore();
}
