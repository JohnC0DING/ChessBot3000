package piece;

import board.Board;
import movement.Move;

import java.util.List;

public abstract class Piece {

    private boolean isWhite;

    private boolean isFriendly;

    public Piece(boolean isWhite, boolean isFriendly) {
        this.isWhite = isWhite;
        this.isFriendly = isFriendly;
    }

    public abstract List<Move> getPossibleMoves(int index, Board board);

    public abstract boolean canSeeOpponentPiece(int currentIndex, int opponentIndex, Board board);

    public boolean isFriendly() {
        return isFriendly;
    }

    public boolean isOpponent(boolean isWhite) {
        return !isFriendly;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public abstract int getScore();

    public abstract Pieces getCharacterRepresentation();
}
