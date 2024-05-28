package piece;

import board.Board;
import movement.Move;
import movement.PieceMoveUtil;
import util.Pair;

import java.util.List;
import java.util.Map;

public class Queen extends Piece {

    public Queen(boolean isWhite, boolean isFriendly) {
        super(isWhite, isFriendly);
    }

    @Override
    public List<Pair<Integer, Move>> getPossibleMoves(int index, Board board) {
        return PieceMoveUtil.getPossibleMovesForSlidingPiece(index, board, SlidingType.BOTH);
    }

    @Override
    public int getScore() {
        return 9;
    }

}
