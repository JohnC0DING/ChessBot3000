package movement;

import board.Board;
import piece.Piece;
import piece.SlidingType;
import util.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class PieceMoveUtil {

    private static final List<Integer> COL_A = Arrays.asList(0, 8, 16, 24, 32, 40, 48, 56);

    private static final List<Integer> COL_H = Arrays.asList(7, 15, 23, 31, 39, 47, 55, 63);

    private static final List<Integer> ROW_1 = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);

    private static final List<Integer> ROW_8 = Arrays.asList(56, 57, 58, 59, 60, 61, 62, 63);

    private static final List<Integer> COL_B = Arrays.asList(1, 9, 17, 25, 33, 41, 49, 57);

    private static final List<Integer> COL_G = Arrays.asList(6, 14, 22, 30, 38, 46, 54, 62);

    private static final List<Integer> ROW_2 = Arrays.asList(8, 9, 10, 11, 12, 13, 14, 15);

    private static final List<Integer> ROW_7 = Arrays.asList(48, 49, 50, 51, 52, 53, 54, 55);

    private static final Predicate<Integer> IS_NOT_FIRST_COL = index -> !COL_A.contains(index);

    private static final Predicate<Integer> IS_NOT_LAST_COL = index -> !COL_H.contains(index);

    private static final Predicate<Integer> IS_NOT_FIRST_ROW = index -> !ROW_1.contains(index);

    private static final Predicate<Integer> IS_NOT_LAST_ROW = index -> !ROW_8.contains(index);

    private static final Function<Integer, Integer> MOVE_MODIFIER_RIGHT = index -> index + 1;

    private static final Function<Integer, Integer> MOVE_MODIFIER_LEFT = index -> index - 1;

    private static final Function<Integer, Integer> MOVE_MODIFIER_UP = index -> index + 8;

    private static final Function<Integer, Integer> MOVE_MODIFIER_DOWN = index -> index - 8;

    public static List<Pair<Integer, Move>> getPossibleMovesForSlidingPiece(int position, Board board, SlidingType type) {
        List<Pair<Integer, Move>> possibleMoves = new ArrayList<>();
        if (type == SlidingType.STRAIGHT || type == SlidingType.BOTH) {
            possibleMoves.addAll(getPossibleMovesForDirection(position, board, IS_NOT_LAST_COL, MOVE_MODIFIER_RIGHT));
            possibleMoves.addAll(getPossibleMovesForDirection(position, board, IS_NOT_FIRST_COL, MOVE_MODIFIER_LEFT));
            possibleMoves.addAll(getPossibleMovesForDirection(position, board, IS_NOT_LAST_ROW, MOVE_MODIFIER_UP));
            possibleMoves.addAll(getPossibleMovesForDirection(position, board, IS_NOT_FIRST_ROW, MOVE_MODIFIER_DOWN));
        }
        if (type == SlidingType.DIAGONAL || type == SlidingType.BOTH) {
            possibleMoves.addAll(getPossibleMovesForDirection(position, board, IS_NOT_LAST_ROW.and(IS_NOT_LAST_COL), MOVE_MODIFIER_UP.andThen(MOVE_MODIFIER_RIGHT)));
            possibleMoves.addAll(getPossibleMovesForDirection(position, board, IS_NOT_LAST_ROW.and(IS_NOT_FIRST_COL), MOVE_MODIFIER_UP.andThen(MOVE_MODIFIER_LEFT)));
            possibleMoves.addAll(getPossibleMovesForDirection(position, board, IS_NOT_FIRST_ROW.and(IS_NOT_LAST_COL), MOVE_MODIFIER_DOWN.andThen(MOVE_MODIFIER_RIGHT)));
            possibleMoves.addAll(getPossibleMovesForDirection(position, board, IS_NOT_FIRST_ROW.and(IS_NOT_FIRST_COL), MOVE_MODIFIER_DOWN.andThen(MOVE_MODIFIER_LEFT)));
        }

        return possibleMoves;
    }

    public static boolean canSeeIndexForSlidingPiece(int index, Board board, SlidingType type, int indexToFind) {
        if (type == SlidingType.STRAIGHT || type == SlidingType.BOTH) {
            if (canSeeIndexInDirection(index, indexToFind, board, IS_NOT_LAST_COL, MOVE_MODIFIER_RIGHT) ||
                    canSeeIndexInDirection(index, indexToFind, board, IS_NOT_FIRST_COL, MOVE_MODIFIER_LEFT) ||
                    canSeeIndexInDirection(index, indexToFind, board, IS_NOT_LAST_ROW, MOVE_MODIFIER_UP) ||
                    canSeeIndexInDirection(index, indexToFind, board, IS_NOT_FIRST_ROW, MOVE_MODIFIER_DOWN)) {
                return true;
            }
        }
        if (type == SlidingType.DIAGONAL || type == SlidingType.BOTH) {
            return canSeeIndexInDirection(index, indexToFind, board, IS_NOT_LAST_ROW.and(IS_NOT_LAST_COL), MOVE_MODIFIER_UP.andThen(MOVE_MODIFIER_RIGHT)) ||
                    canSeeIndexInDirection(index, indexToFind, board, IS_NOT_LAST_ROW.and(IS_NOT_FIRST_COL), MOVE_MODIFIER_UP.andThen(MOVE_MODIFIER_LEFT)) ||
                    canSeeIndexInDirection(index, indexToFind, board, IS_NOT_FIRST_ROW.and(IS_NOT_LAST_COL), MOVE_MODIFIER_DOWN.andThen(MOVE_MODIFIER_RIGHT)) ||
                    canSeeIndexInDirection(index, indexToFind, board, IS_NOT_FIRST_ROW.and(IS_NOT_FIRST_COL), MOVE_MODIFIER_DOWN.andThen(MOVE_MODIFIER_LEFT));
        }

        return false;
    }

    public static boolean canSeeIndexInDirection(int currentIndex, int indexToFind, Board board, Predicate<Integer> isNotEndOfSearch, Function<Integer, Integer> moveModifier) {
        int moveIndex = currentIndex;
        while (isNotEndOfSearch.test(moveIndex)) {
            moveIndex = moveModifier.apply(moveIndex);

            Optional<Piece> maybePiece = board.findPieceByIndex(moveIndex);

            if (maybePiece.isPresent()) {
                //If the piece is the one we are looking for return true, otherwise stop looking in this direction
                return indexToFind == moveIndex;
            }
        }

        return false;
    }

    public static List<Pair<Integer, Move>> getPossibleMovesForDirection(int currentIndex, Board board, Predicate<Integer> isNotEndOfSearch, Function<Integer, Integer> moveModifier) {
        List<Pair<Integer, Move>> possibleMoves = new ArrayList<>();

        int moveIndex = currentIndex;
        while (isNotEndOfSearch.test(moveIndex)) {
            moveIndex = moveModifier.apply(moveIndex);
            Optional<Piece> maybePiece = board.findPieceByIndex(moveIndex);

            if (maybePiece.isPresent()) {
                //Add move if we can take the piece
                if (!maybePiece.get().isFriendly()) {
                    possibleMoves.add(new Pair<>(getScoreForMove(board, moveIndex), new Move(currentIndex, moveIndex)));
                }
                //Don't look any further down this line as we are blocked by an opponent or friendly piece.
                break;
            } else {
                possibleMoves.add(new Pair<>(Board.EMPTY_TILE_SCORE, new Move(currentIndex, moveIndex)));
            }
        }

        return possibleMoves;
    }

    public static List<Pair<Integer, Move>> getPossibleMovesForKing(int currentIndex, Board board) {
        List<Pair<Integer, Move>> possibleMoves = new ArrayList<>();

        if (IS_NOT_FIRST_COL.test(currentIndex)) {
            int moveIndex = MOVE_MODIFIER_LEFT.apply(currentIndex);
            possibleMoves.add(new Pair<>(getScoreForMove(board, moveIndex), new Move(currentIndex, moveIndex)));
        }
        if (IS_NOT_LAST_COL.test(currentIndex)) {
            int moveIndex = MOVE_MODIFIER_RIGHT.apply(currentIndex);
            possibleMoves.add(new Pair<>(getScoreForMove(board, moveIndex), new Move(currentIndex, moveIndex)));
        }
        if (IS_NOT_FIRST_ROW.test(currentIndex)) {
            int moveIndex = MOVE_MODIFIER_DOWN.apply(currentIndex);
            possibleMoves.add(new Pair<>(getScoreForMove(board, moveIndex), new Move(currentIndex, moveIndex)));
        }
        if (IS_NOT_LAST_ROW.test(currentIndex)) {
            int moveIndex = MOVE_MODIFIER_UP.apply(currentIndex);
            possibleMoves.add(new Pair<>(getScoreForMove(board, moveIndex), new Move(currentIndex, moveIndex)));
        }
        if (IS_NOT_FIRST_COL.and(IS_NOT_FIRST_ROW).test(currentIndex)) {
            int moveIndex = MOVE_MODIFIER_LEFT.andThen(MOVE_MODIFIER_DOWN).apply(currentIndex);
            possibleMoves.add(new Pair<>(getScoreForMove(board, moveIndex), new Move(currentIndex, moveIndex)));
        }
        if (IS_NOT_FIRST_COL.and(IS_NOT_LAST_ROW).test(currentIndex)) {
            int moveIndex = MOVE_MODIFIER_LEFT.andThen(MOVE_MODIFIER_UP).apply(currentIndex);
            possibleMoves.add(new Pair<>(getScoreForMove(board, moveIndex), new Move(currentIndex, moveIndex)));
        }
        if (IS_NOT_LAST_COL.and(IS_NOT_FIRST_ROW).test(currentIndex)) {
            int moveIndex = MOVE_MODIFIER_RIGHT.andThen(MOVE_MODIFIER_DOWN).apply(currentIndex);
            possibleMoves.add(new Pair<>(getScoreForMove(board, moveIndex), new Move(currentIndex, moveIndex)));
        }
        if (IS_NOT_LAST_COL.and(IS_NOT_LAST_ROW).test(currentIndex)) {
            int moveIndex = MOVE_MODIFIER_RIGHT.andThen(MOVE_MODIFIER_UP).apply(currentIndex);
            possibleMoves.add(new Pair<>(getScoreForMove(board, moveIndex), new Move(currentIndex, moveIndex)));
        }

        return possibleMoves;
    }

    public static boolean canSeeIndexForKing(int currentIndex, int indexToFind, Board board) {
        List<Pair<Integer, Move>> possibleMoves = new ArrayList<>();

        if (IS_NOT_FIRST_COL.test(currentIndex)) {
            int moveIndex = MOVE_MODIFIER_LEFT.apply(currentIndex);
            if (moveIndex == indexToFind) {
                return true;
            }
        }
        if (IS_NOT_LAST_COL.test(currentIndex)) {
            int moveIndex = MOVE_MODIFIER_RIGHT.apply(currentIndex);
            if (moveIndex == indexToFind) {
                return true;
            }
        }
        if (IS_NOT_FIRST_ROW.test(currentIndex)) {
            int moveIndex = MOVE_MODIFIER_DOWN.apply(currentIndex);
            if (moveIndex == indexToFind) {
                return true;
            }
        }
        if (IS_NOT_LAST_ROW.test(currentIndex)) {
            int moveIndex = MOVE_MODIFIER_UP.apply(currentIndex);
            if (moveIndex == indexToFind) {
                return true;
            }
        }
        if (IS_NOT_FIRST_COL.and(IS_NOT_FIRST_ROW).test(currentIndex)) {
            int moveIndex = MOVE_MODIFIER_LEFT.andThen(MOVE_MODIFIER_DOWN).apply(currentIndex);
            if (moveIndex == indexToFind) {
                return true;
            }
        }
        if (IS_NOT_FIRST_COL.and(IS_NOT_LAST_ROW).test(currentIndex)) {
            int moveIndex = MOVE_MODIFIER_LEFT.andThen(MOVE_MODIFIER_UP).apply(currentIndex);
            if (moveIndex == indexToFind) {
                return true;
            }
        }
        if (IS_NOT_LAST_COL.and(IS_NOT_FIRST_ROW).test(currentIndex)) {
            int moveIndex = MOVE_MODIFIER_RIGHT.andThen(MOVE_MODIFIER_DOWN).apply(currentIndex);
            if (moveIndex == indexToFind) {
                return true;
            }
        }
        if (IS_NOT_LAST_COL.and(IS_NOT_LAST_ROW).test(currentIndex)) {
            int moveIndex = MOVE_MODIFIER_RIGHT.andThen(MOVE_MODIFIER_UP).apply(currentIndex);
            return moveIndex == indexToFind;
        }

        return false;
    }

    //Could continue to use sets here or could use a map of index to piece, might be quicker look up?
    public static List<Pair<Integer, Move>> getPossibleMovesForPawn(int currentIndex, Board board, boolean isWhite) {

        List<Pair<Integer, Move>> possibleMoves = new ArrayList<>();
        Function<Integer, Integer> moveModifierForward = isWhite ? MOVE_MODIFIER_UP : MOVE_MODIFIER_DOWN;
        List<Integer> startingRow = isWhite ? ROW_2 : ROW_7;

        if (currentIndex / 56 >= 1) {
            //im a queen now this should never happen
            return null;
        }

        int forwardMove = moveModifierForward.apply(currentIndex);
        if (board.findPieceByIndex(forwardMove).isEmpty()) {
            possibleMoves.add(new Pair<>(Board.EMPTY_TILE_SCORE, new Move(currentIndex, forwardMove)));
        }
        int leftDiagonalTake = MOVE_MODIFIER_LEFT.andThen(moveModifierForward).apply(currentIndex);
        if (board.findPieceByIndex(leftDiagonalTake).isPresent() && board.findPieceByIndex(leftDiagonalTake).get().isFriendly()) {
            possibleMoves.add(new Pair<>(getScoreForMove(board, leftDiagonalTake), new Move(currentIndex, leftDiagonalTake)));
        }
        int rightDiagonalTake = MOVE_MODIFIER_RIGHT.andThen(moveModifierForward).apply(currentIndex);
        if (board.findPieceByIndex(rightDiagonalTake).isPresent() && board.findPieceByIndex(rightDiagonalTake).get().isFriendly()) {
            possibleMoves.add(new Pair<>(getScoreForMove(board, rightDiagonalTake), new Move(currentIndex, rightDiagonalTake)));
        }

        //Starting tile double move
        if (startingRow.contains(currentIndex)) {
            int doubleForwardMove = moveModifierForward.apply(forwardMove);
            if (board.findPieceByIndex(doubleForwardMove).isEmpty()) {
                possibleMoves.add(new Pair<>(Board.EMPTY_TILE_SCORE, new Move(currentIndex, doubleForwardMove)));
            }
        }

        return possibleMoves;
    }


    //Could continue to use sets here or could use a map of index to piece, might be quicker look up?
    public static boolean canSeeIndexForPawn(int currentIndex, int indexToFind, Board board, boolean isWhite) {

        Function<Integer, Integer> moveModifierForward = isWhite ? MOVE_MODIFIER_UP : MOVE_MODIFIER_DOWN;
        List<Integer> startingRow = isWhite ? Arrays.asList(8, 9, 10, 11, 12, 13, 14, 15) : Arrays.asList(48, 49, 50, 51, 52, 53, 54, 55);

        if (currentIndex / 56 >= 1) {
            //im a queen now this should never happen
            return false;
        }

        int forwardMove = moveModifierForward.apply(currentIndex);
        if (board.findPieceByIndex(forwardMove).isEmpty()) {
            if (forwardMove == indexToFind) {
                return true;
            }
        }
        int leftDiagonalTake = MOVE_MODIFIER_LEFT.andThen(moveModifierForward).apply(currentIndex);
        if (board.findPieceByIndex(leftDiagonalTake).isPresent() && board.findPieceByIndex(leftDiagonalTake).get().isFriendly()) {
            if (leftDiagonalTake == indexToFind) {
                return true;
            }        }
        int rightDiagonalTake = MOVE_MODIFIER_RIGHT.andThen(moveModifierForward).apply(currentIndex);
        if (board.findPieceByIndex(rightDiagonalTake).isPresent() && board.findPieceByIndex(rightDiagonalTake).get().isFriendly()) {
            if (rightDiagonalTake == indexToFind) {
                return true;
            }        }

        //Starting tile double move
        if (startingRow.contains(currentIndex)) {
            int doubleForwardMove = moveModifierForward.apply(forwardMove);
            return board.findPieceByIndex(doubleForwardMove).isEmpty() && doubleForwardMove == indexToFind;
        }

        return false;
    }

    public static boolean canSeeIndexForKnight(int currentIndex, int indexToFind, Board board) {

        if (!COL_B.contains(currentIndex)) {
            if (IS_NOT_LAST_ROW.test(currentIndex)) {
                int moveIndex = currentIndex + 6;
                if (moveIndex == indexToFind) {
                    return true;
                }            }
            if (IS_NOT_FIRST_ROW.test(currentIndex)) {
                int moveIndex = currentIndex - 10;
                if (moveIndex == indexToFind) {
                    return true;
                }            }

        }
        if (!COL_G.contains(currentIndex)) {
            if (IS_NOT_LAST_ROW.test(currentIndex)) {
                int moveIndex = currentIndex + 10;
                if (moveIndex == indexToFind) {
                    return true;
                }            }
            if (IS_NOT_FIRST_ROW.test(currentIndex)) {
                int moveIndex = currentIndex - 6;
                if (moveIndex == indexToFind) {
                    return true;
                }
            }
        }
        if (!ROW_2.contains(currentIndex)) {
            if (IS_NOT_LAST_COL.test(currentIndex)) {
                int moveIndex = currentIndex - 15;
                if (moveIndex == indexToFind) {
                    return true;
                }
            }
            if (IS_NOT_FIRST_COL.test(currentIndex)) {
                int moveIndex = currentIndex - 17;
                if (moveIndex == indexToFind) {
                    return true;
                }
            }
        }
        if (!ROW_7.contains(currentIndex)) {
            if (IS_NOT_LAST_COL.test(currentIndex)) {
                int moveIndex = currentIndex + 17;
                if (moveIndex == indexToFind) {
                    return true;
                }
            }
            if (IS_NOT_FIRST_COL.test(currentIndex)) {
                int moveIndex = currentIndex + 15;
                if (moveIndex == indexToFind) {
                    return true;
                }
            }
        }

        return false;
    }

    public static List<Pair<Integer, Move>> getPossibleMovesForKnight(int currentIndex, Board board) {

        List<Pair<Integer, Move>> possibleMoves = new ArrayList<>();

        if (!COL_B.contains(currentIndex)) {
            if (IS_NOT_LAST_ROW.test(currentIndex)) {
                int moveIndex = currentIndex + 6;
                possibleMoves.add(new Pair<>(getScoreForMove(board, moveIndex), new Move(currentIndex, moveIndex)));
            }
            if (IS_NOT_FIRST_ROW.test(currentIndex)) {
                int moveIndex = currentIndex - 10;
                possibleMoves.add(new Pair<>(getScoreForMove(board, moveIndex), new Move(currentIndex, moveIndex)));
            }

        }
        if (!COL_G.contains(currentIndex)) {
            if (IS_NOT_LAST_ROW.test(currentIndex)) {
                int moveIndex = currentIndex + 10;
                possibleMoves.add(new Pair<>(getScoreForMove(board, moveIndex), new Move(currentIndex, moveIndex)));
            }
            if (IS_NOT_FIRST_ROW.test(currentIndex)) {
                int moveIndex = currentIndex - 6;
                possibleMoves.add(new Pair<>(getScoreForMove(board, moveIndex), new Move(currentIndex, moveIndex)));
            }
        }
        if (!ROW_2.contains(currentIndex)) {
            if (IS_NOT_LAST_COL.test(currentIndex)) {
                int moveIndex = currentIndex - 15;
                possibleMoves.add(new Pair<>(getScoreForMove(board, moveIndex), new Move(currentIndex, moveIndex)));
            }
            if (IS_NOT_FIRST_COL.test(currentIndex)) {
                int moveIndex = currentIndex - 17;
                possibleMoves.add(new Pair<>(getScoreForMove(board, moveIndex), new Move(currentIndex, moveIndex)));
            }
        }
        if (!ROW_7.contains(currentIndex)) {
            if (IS_NOT_LAST_COL.test(currentIndex)) {
                int moveIndex = currentIndex + 17;
                possibleMoves.add(new Pair<>(getScoreForMove(board, moveIndex), new Move(currentIndex, moveIndex)));
            }
            if (IS_NOT_FIRST_COL.test(currentIndex)) {
                int moveIndex = currentIndex + 15;
                possibleMoves.add(new Pair<>(getScoreForMove(board, moveIndex), new Move(currentIndex, moveIndex)));
            }
        }

        return possibleMoves;
    }

    public static boolean canSeeOpponentPiece(int index, boolean isWhite, Board board) {


        return
    }

//    public static boolean canSeeOpponentPiece(int index, boolean isWhite, Board board){
//        List<Piece> opponentPieces = board.getOpponentPieceLocations(isWhite)
//                .stream()
//                .map(opponentIndex -> {
//                    board.getPieceByIndex(opponentIndex)
//                            .getPossibleMoves(opponentIndex, board).contains(index);
//                })
//                ;
//
//        return opponentPieces.forEach(piece -> piece.getPossibleMoves());
//    }

    //Move to own util or service and make better
    private static Integer getScoreForMove(Board board, int moveIndex) {
        Optional<Piece> maybePiece = board.findPieceByIndex(moveIndex);
        return maybePiece.map(Piece::getScore).orElse(Board.EMPTY_TILE_SCORE);
    }
}
