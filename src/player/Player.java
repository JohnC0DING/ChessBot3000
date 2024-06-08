package player;

import board.Board;
import movement.Move;

public interface Player<T> {

    T resolveMove(Board board) throws InterruptedException;

    boolean isWhite();
}
