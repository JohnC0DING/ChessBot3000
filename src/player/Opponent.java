package player;

import board.Board;
import board.BoardUtil;
import fenfilemanagement.MoveReader;
import movement.Move;
import util.Pair;

public class Opponent implements Player<String> {

    private final boolean isWhite;

    private Bot bot;

    public Opponent(boolean isWhite) {
        this.isWhite = isWhite;
    }

    @Override
    public String resolveMove(Board board) throws InterruptedException {

        return MoveReader.getOpponentMove(isWhite);
    }

    @Override
    public boolean isWhite() {
        return isWhite;
    }
}
