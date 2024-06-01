package player;

import board.Board;
import movement.Move;

public interface Player {

    Move resolveMove(Board board) throws InterruptedException;

    boolean isBot();

    boolean isWhite();
}
