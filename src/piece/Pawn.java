package piece;

import board.Board;
import movement.Move;
import movement.PieceMoveUtil;
import util.Pair;

import java.util.List;
import java.util.Map;

public class Pawn extends Piece {

    public Pawn(boolean isWhite, boolean isFriendly) {
        super(isWhite, isFriendly);
    }

    @Override
    public List<Move> getPossibleMoves(int index, Board board) {
        return PieceMoveUtil.getPossibleMovesForPawn(index, board, super.isWhite());
    }

    @Override
    public boolean canSeeOpponentPiece(int currentIndex, int opponentIndex, Board board) {
        return PieceMoveUtil.canSeeIndexForPawn(currentIndex, currentIndex, board, super.isWhite());
    }

    @Override
    public int getScore() {
        return 1;
    }

    @Override
    public Pieces getCharacterRepresentation(){
        if(isWhite()){
            return Pieces.P;
        }
        return Pieces.p;
    }
}
