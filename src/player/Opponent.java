package player;

import board.Board;
import fenfilemanagement.MoveReader;
import movement.Move;
import util.Pair;

public class Opponent {

    private final boolean isWhite;

    private final boolean isBot;

    public Opponent(boolean isWhite, boolean isBot) {
        this.isWhite = isWhite;
        this.isBot = isBot;
    }

    public Pair<String, Move> makeMove(Board board) throws InterruptedException {
//        if(isBot){
//            return  "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2";
//        }
//        return MoveReader.getOpponentMove(isWhite);
        return null;
        //to be implemented
    }

    public boolean isBot() {
        return isBot;
    }
}
