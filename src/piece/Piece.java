package piece;

import board.Board;

import java.util.Map;

public interface Piece {

    Map<Integer, Integer> getPossibleMoves(int position, Board board);

    boolean isFriendly();

    boolean isWhite();

    int getScore();
}
