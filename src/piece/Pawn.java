package piece;

import board.Board;

import java.util.Map;

public class Pawn implements Piece{

    private boolean isQueened = false;

    private boolean isWhite;

    private boolean isFriendly;

    public Pawn(boolean isWhite, boolean isFriendly) {
        this.isWhite = isWhite;
        this.isFriendly = isFriendly;
    }

    //Could continue to use sets here or could use a map of index to piece, might be quicker look up?
    @Override
    public Map<Integer, Integer> getPossibleMoves(int currentPosition, Board board) {
//
//        Set<Integer> possibleMoves = new HashSet<>();
//        Set<Integer> allPieceLocations = new HashSet<>(opponentPieceLocations);
//        allPieceLocations.addAll(friendlyPieceLocations);
//
//        if(currentPosition / 56 >= 1){
//            //im a queen now this should never happen
//            return null;
//        }
//
//        int forwardMove = currentPosition + 8;
//        if(!allPieceLocations.contains(forwardMove)){
//            possibleMoves.add(forwardMove);
//        }
//        int leftDiagonalTake = currentPosition + 7;
//        if(opponentPieceLocations.contains(leftDiagonalTake)){
//            possibleMoves.add(leftDiagonalTake);
//        }
//        int rightDiagonalTake = currentPosition + 9;
//        if(opponentPieceLocations.contains(leftDiagonalTake)){
//            possibleMoves.add(rightDiagonalTake);
//        }
//
//        return possibleMoves;
        return null;
    }

    @Override
    public boolean isFriendly() {
        return false;
    }

    @Override
    public int getScore() {
        return 1;
    }

    public void setQueened(boolean queened) {
        isQueened = queened;
    }

    public boolean isQueened() {
        return isQueened;
    }

    @Override
    public boolean isWhite() {
        return isWhite;
    }
}
