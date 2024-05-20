package piece;

import board.Board;
import util.Pair;

import java.util.Map;

public interface Piece {

    Map<Pair<Integer, Integer>, Integer> getPossibleMoves(int index, Board board);

    boolean isFriendly();

    boolean isWhite();

    int getScore();
}
