package player;

import board.Board;
import board.BoardUtil;
import fenfilemanagement.MoveReader;
import movement.Move;
import util.Pair;

public class Opponent implements Player{

    private final boolean isWhite;

    private final boolean isBot;

    private Bot bot;

    public Opponent(boolean isWhite, boolean isBot) {
        this.isWhite = isWhite;
        this.isBot = isBot;
    }

    public Move resolveMove(Board board) throws InterruptedException {
        if(isBot){
            bot.resolveMove(board);
        }
        return MoveReader.getOpponentMove(isWhite);
        //to be implemented
    }

    public boolean isBot() {
        return isBot;
    }

    @Override
    public boolean isWhite() {
        return isWhite;
    }
}
