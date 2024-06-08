package player;

import board.Board;
import fenfilemanagement.MoveWriter;
import movement.Move;
import movement.MoveTreeService;
import util.Pair;

import java.util.HashMap;
import java.util.Map;

public class Bot implements Player{

    private final boolean isWhite;

    private final boolean isFriendly;

    public Bot(boolean isWhite, boolean isFriendly, MoveTreeService moveTreeService) {
        this.isWhite = isWhite;
        this.isFriendly = isFriendly;
        this.moveTreeService = moveTreeService;
    }

    private boolean isFirstMove = true;

    MoveTreeService moveTreeService;

    public Move resolveMove(Board board) {
        if(isFirstMove){
            isFirstMove = false;
        }
        moveTreeService.constructMoveTree(isWhite, isFriendly, board);

        return moveTreeService.resolveBestMove(isWhite, isFriendly, board);
    }

    public boolean isWhite() {
        return isWhite;
    }
}
