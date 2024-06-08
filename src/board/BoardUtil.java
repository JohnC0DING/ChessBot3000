package board;

import movement.Move;
import piece.King;
import piece.Piece;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BoardUtil {

    private static final double CENTRAL_CONTROL_WEIGHT_VAL = 0.5;

    private static final List<Integer> CENTRAL_SQUARES = Arrays.asList(34, 35, 37, 26, 27, 28);

    public static Double getScoreForBoardState(Board board, boolean isWhiteToMove){
        double score = 0;
        int friendlyKingLocation = -1;
        int opponentKingLocation = -1;

        for (Integer index : board.getFriendlyPieceLocations(isWhiteToMove)) {
            Piece piece = board.getPieceByIndex(index);
            score += addMoveOpportunityScore(board, index, piece);
            score += piece.getScore();
            if(piece instanceof King){
                friendlyKingLocation = index;
            }
        }
        for (Integer index : board.getOpponentPieceLocations(isWhiteToMove)) {
            Piece piece = board.getPieceByIndex(index);
            score -= addMoveOpportunityScore(board, index, piece);
            score -= piece.getScore();
            if(piece instanceof King){
                opponentKingLocation = index;
            }
        }

        final int finalOpponentKingLocation = opponentKingLocation;
        if(board.getFriendlyPieceLocations(isWhiteToMove)
                .stream()
                .anyMatch(index -> board.getPieceByIndex(index).canSeeOpponentPiece(index, finalOpponentKingLocation, board))){
            score += 10;
        }

        final int finalFriendlyKingLocation = friendlyKingLocation;
        if(board.getOpponentPieceLocations(isWhiteToMove)
                .stream()
                .anyMatch(index -> board.getPieceByIndex(index).canSeeOpponentPiece(index, finalFriendlyKingLocation, board))){
            score -= 10;
        }

        return score;
    }

    private static double addMoveOpportunityScore(Board board, Integer index, Piece piece) {
        List<Move> possibleMoves = piece.getPossibleMoves(index, board);
        double centralControlOpportunity = possibleMoves.stream().map(move -> move.getMove().right()).anyMatch(CENTRAL_SQUARES::contains) ? CENTRAL_CONTROL_WEIGHT_VAL : 0;
        return (double) possibleMoves.size() / 100 + centralControlOpportunity;
    }
}
