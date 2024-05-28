package piece;

import board.Board;
import movement.Move;
import movement.PieceMoveUtil;
import util.Pair;

import java.util.List;
import java.util.Map;

public class Pawn extends Piece {

    public Pawn(boolean isWhite, boolean isFriendly) {
        super(isWhite, isFriendly);
    }

    @Override
    public List<Pair<Integer, Move>> getPossibleMoves(int index, Board board) {
        return PieceMoveUtil.getPossibleMovesForPawn(index, board, isWhite);
    }

    @Override
    public boolean isFriendly() {
        return false;
    }

    @Override
    public int getScore() {
        return 1;
    }

    @Override
    public boolean isWhite() {
        return isWhite;
    }
}
