package piece;

import board.Board;
import movement.Move;
import movement.PieceMoveUtil;
import util.Pair;

import java.util.List;
import java.util.Map;

public class Knight extends Piece {

    public Knight(boolean isWhite, boolean isFriendly) {
        super(isWhite, isFriendly);
    }

    @Override
    public List<Move> getPossibleMoves(int index, Board board) {
        return PieceMoveUtil.getPossibleMovesForKnight(index, board);
    }

    @Override
    public boolean canSeeOpponentPiece(int currentIndex, int opponentIndex, Board board) {
        return PieceMoveUtil.canSeeIndexForKnight(currentIndex, opponentIndex, board);
    }

    @Override
    public int getScore() {
        return 3;
    }

    @Override
    public Pieces getCharacterRepresentation(){
        if(isWhite()){
            return Pieces.N;
        }
        return Pieces.n;
    }
}
