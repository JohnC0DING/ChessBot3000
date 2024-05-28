package piece;

import board.Board;
import movement.Move;
import movement.PieceMoveUtil;
import util.Pair;

import java.util.List;

public class King extends Piece{

    public King(boolean isWhite, boolean isFriendly) {
        super(isWhite, isFriendly);
    }

    @Override
    public List<Pair<Integer, Move>> getPossibleMoves(int index, Board board) {
        return PieceMoveUtil.getPossibleMovesForKing(index, board);
    }

    @Override
    public boolean isFriendly() {
        return false;
    }

    @Override
    public boolean isWhite() {
        return false;
    }

    @Override
    public int getScore() {
        return 0;
    }
}
