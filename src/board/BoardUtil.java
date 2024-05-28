package board;

import piece.King;
import piece.Piece;

import java.util.List;
import java.util.Optional;

public class BoardUtil {

    public static Integer getScoreForBoardState(Board board, boolean isBotWhite){
        int score = 0;
        int friendlyKingLocation = -1;
        int opponentKingLocation = -1;

        for (Integer index : board.getFriendlyPieceLocations(isBotWhite)) {
            Piece piece = board.getPieceByIndex(index);
            score += piece.getScore();
            if(piece instanceof King){
                friendlyKingLocation = index;
            }
        }
        for (Integer index : board.getOpponentPieceLocations(isBotWhite)) {
            Piece piece = board.getPieceByIndex(index);
            score -= piece.getScore();
            if(piece instanceof King){
                opponentKingLocation = index;
            }
        }

        final int finalOpponentKingLocation = opponentKingLocation;
        if(board.getFriendlyPieceLocations(isBotWhite)
                .stream()
                .anyMatch(index -> board.getPieceByIndex(index).canSeeOpponentPiece(finalOpponentKingLocation, board))){
            score += 10;
        }

        final int finalFriendlyKingLocation = friendlyKingLocation;
        if(board.getOpponentPieceLocations(isBotWhite)
                .stream()
                .anyMatch(index -> board.getPieceByIndex(index).canSeeOpponentPiece(finalFriendlyKingLocation, board))){
            score -= 10;
        }

        return score;
    }
}
