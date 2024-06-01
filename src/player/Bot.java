package player;

import board.Board;
import fenfilemanagement.MoveWriter;
import movement.Move;
import movement.MoveTreeService;
import util.Pair;

import java.util.HashMap;
import java.util.Map;

public class Bot implements Player{

    private boolean isWhite;

    public Bot(boolean isWhite, MoveTreeService moveTreeService) {
        this.isWhite = isWhite;
        this.moveTreeService = moveTreeService;
    }

    private boolean isFirstMove = true;

    MoveTreeService moveTreeService;

    @Override
    public Move resolveMove(Board board) {
        if(isFirstMove){
            moveTreeService.constructMoveTree(2, isWhite, true, board);
            isFirstMove = false;
        }

        return moveTreeService.resolveBestMove(isWhite, true, board);
    }

    public boolean isWhite() {
        return isWhite;
    }

    @Override
    public boolean isBot() {
        return true;
    }
}
