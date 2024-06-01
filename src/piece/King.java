package piece;

import board.Board;
import movement.Move;
import movement.PieceMoveUtil;
import util.Pair;

import java.util.List;

public class King extends Piece{

    public King(boolean isWhite, boolean isFriendly) {
        super(isWhite, isFriendly);
    }

    @Override
    public List<Move> getPossibleMoves(int index, Board board) {
        return PieceMoveUtil.getPossibleMovesForKing(index, board);
    }

    @Override
    public boolean canSeeOpponentPiece(int currentIndex, int opponentIndex, Board board) {
        return PieceMoveUtil.canSeeIndexForKing(currentIndex, opponentIndex, board);
    }

    @Override
    public int getScore() {
        return 0;
    }

    @Override
    public Pieces getCharacterRepresentation(){
        if(isWhite()){
            return Pieces.K;
        }
        return Pieces.k;
    }
}
