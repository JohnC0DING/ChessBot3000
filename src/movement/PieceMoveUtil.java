package movement;

import board.Board;
import piece.King;
import piece.Piece;
import piece.Rook;
import piece.SlidingType;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.IntStream;

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

    public static List<Move> getPossibleMovesForSlidingPiece(int position, Board board, SlidingType type) {
        List<Move> possibleMoves = new ArrayList<>();
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

    public static boolean canSeeIndexForSlidingPiece(int index, int indexToFind, Board board, SlidingType type) {
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

    public static List<Move> getPossibleMovesForDirection(int currentIndex, Board board, Predicate<Integer> isNotEndOfSearch, Function<Integer, Integer> moveModifier) {
        List<Move> possibleMoves = new ArrayList<>();

        int moveIndex = currentIndex;
        while (isNotEndOfSearch.test(moveIndex)) {
            moveIndex = moveModifier.apply(moveIndex);
            Piece currentPiece = board.getPieceByIndex(currentIndex);
            Optional<Piece> maybePiece = board.findPieceByIndex(moveIndex);

            if (maybePiece.isPresent()) {
                //Add move if we can take the piece
                if (maybePiece.get().isWhite() && currentPiece.isWhite()) {
                    possibleMoves.add(new Move(currentIndex, moveIndex));
                }
                //Don't look any further down this line as we are blocked by an opponent or friendly piece.
                break;
            } else {
                possibleMoves.add(new Move(currentIndex, moveIndex));
            }
        }

        return possibleMoves;
    }

    public static List<Move> getPossibleMovesForKing(int currentIndex, Board board, King king) {
        List<Move> possibleMoves = new ArrayList<>();

        if (IS_NOT_FIRST_COL.test(currentIndex)) {
            int moveIndex = MOVE_MODIFIER_LEFT.apply(currentIndex);
            addMoveIfEmptyOrOpponent(currentIndex, board, moveIndex, possibleMoves);
        }
        if (IS_NOT_LAST_COL.test(currentIndex)) {
            int moveIndex = MOVE_MODIFIER_RIGHT.apply(currentIndex);
            addMoveIfEmptyOrOpponent(currentIndex, board, moveIndex, possibleMoves);
        }
        if (IS_NOT_FIRST_ROW.test(currentIndex)) {
            int moveIndex = MOVE_MODIFIER_DOWN.apply(currentIndex);
            addMoveIfEmptyOrOpponent(currentIndex, board, moveIndex, possibleMoves);
        }
        if (IS_NOT_LAST_ROW.test(currentIndex)) {
            int moveIndex = MOVE_MODIFIER_UP.apply(currentIndex);
            addMoveIfEmptyOrOpponent(currentIndex, board, moveIndex, possibleMoves);
        }
        if (IS_NOT_FIRST_COL.and(IS_NOT_FIRST_ROW).test(currentIndex)) {
            int moveIndex = MOVE_MODIFIER_LEFT.andThen(MOVE_MODIFIER_DOWN).apply(currentIndex);
            addMoveIfEmptyOrOpponent(currentIndex, board, moveIndex, possibleMoves);
        }
        if (IS_NOT_FIRST_COL.and(IS_NOT_LAST_ROW).test(currentIndex)) {
            int moveIndex = MOVE_MODIFIER_LEFT.andThen(MOVE_MODIFIER_UP).apply(currentIndex);
            addMoveIfEmptyOrOpponent(currentIndex, board, moveIndex, possibleMoves);
        }
        if (IS_NOT_LAST_COL.and(IS_NOT_FIRST_ROW).test(currentIndex)) {
            int moveIndex = MOVE_MODIFIER_RIGHT.andThen(MOVE_MODIFIER_DOWN).apply(currentIndex);
            addMoveIfEmptyOrOpponent(currentIndex, board, moveIndex, possibleMoves);
        }
        if (IS_NOT_LAST_COL.and(IS_NOT_LAST_ROW).test(currentIndex)) {
            int moveIndex = MOVE_MODIFIER_RIGHT.andThen(MOVE_MODIFIER_UP).apply(currentIndex);
            addMoveIfEmptyOrOpponent(currentIndex, board, moveIndex, possibleMoves);
        }

        addCastlingMoves(currentIndex, board, king, possibleMoves);

        return possibleMoves;
    }



    private static void addCastlingMoves(int currentIndex, Board board, King king, List<Move> possibleMoves) {
        if (!king.hasMoved()) {
            if (king.isWhite()) {
                addCastlingMove(board, currentIndex, 56,
                        MOVE_MODIFIER_LEFT.andThen(MOVE_MODIFIER_LEFT),
                        MOVE_MODIFIER_RIGHT, possibleMoves, true);
                addCastlingMove(board, currentIndex, 63,
                        MOVE_MODIFIER_RIGHT.andThen(MOVE_MODIFIER_RIGHT),
                        MOVE_MODIFIER_LEFT, possibleMoves, true);
            } else {
                addCastlingMove(board, currentIndex, 0,
                        MOVE_MODIFIER_LEFT.andThen(MOVE_MODIFIER_LEFT),
                        MOVE_MODIFIER_RIGHT, possibleMoves, false);
                addCastlingMove(board, currentIndex, 7,
                        MOVE_MODIFIER_RIGHT.andThen(MOVE_MODIFIER_RIGHT),
                        MOVE_MODIFIER_LEFT, possibleMoves, false);
            }
        }
    }

//
//    private static void addCastleMoves(int currentIndex, Board board, King king, List<Move> possibleMoves) {
//        if (!king.hasMoved()) {
//            if (king.isWhite()) {
//                int currentIndexForRook = 56;
//                Optional<Piece> maybePiece = board.findPieceByIndex(currentIndexForRook);
//                if (maybePiece.isPresent() && maybePiece.get() instanceof Rook && !((Rook) maybePiece.get()).hasMoved()) {
//                    if (IntStream.range(57, 61)
//                            .anyMatch(index -> isBlockedByPiece(board, index) ||
//                                    isMovingThroughCheckOrIsInCheck(board, index))) {
//                        return;
//                    }
//                    int destinationIndexForKing = MOVE_MODIFIER_LEFT.andThen(MOVE_MODIFIER_LEFT).apply(currentIndex);
//                    int destinationIndexForRook = MOVE_MODIFIER_RIGHT.apply(destinationIndexForKing);
//                    possibleMoves.add(new Move(currentIndex, destinationIndexForKing, currentIndexForRook, destinationIndexForRook));
//                }
//                currentIndexForRook = 63;
//                maybePiece = board.findPieceByIndex(currentIndexForRook);
//                if (maybePiece.isPresent() && maybePiece.get() instanceof Rook && !((Rook) maybePiece.get()).hasMoved()) {
//                    if (IntStream.range(57, 61)
//                            .anyMatch(index -> isBlockedByPiece(board, index) ||
//                                    isMovingThroughCheckOrIsInCheck(board, index))) {
//                        return;
//                    }
//                    int destinationIndexForKing = MOVE_MODIFIER_RIGHT.andThen(MOVE_MODIFIER_RIGHT).apply(currentIndex);
//                    int destinationIndexForRook = MOVE_MODIFIER_LEFT.apply(destinationIndexForKing);
//                    possibleMoves.add(new Move(currentIndex, destinationIndexForKing, currentIndexForRook, destinationIndexForRook));
//                }
//            } else {
//                int currentIndexForRook = 63;
//                Optional<Piece> maybePiece = board.findPieceByIndex(currentIndexForRook);
//                if (maybePiece.isPresent() && maybePiece.get() instanceof Rook && !((Rook) maybePiece.get()).hasMoved()) {
//                    if (IntStream.range(57, 61)
//                            .anyMatch(index -> isBlockedByPiece(board, index) ||
//                                    isMovingThroughCheckOrIsInCheck(board, index))) {
//                        return;
//                    }
//                    int destinationIndexForKing = MOVE_MODIFIER_RIGHT.andThen(MOVE_MODIFIER_RIGHT).apply(currentIndex);
//                    int destinationIndexForRook = MOVE_MODIFIER_LEFT.apply(destinationIndexForKing);
//                    possibleMoves.add(new Move(currentIndex, destinationIndexForKing, currentIndexForRook, destinationIndexForRook));
//                }
//            }
//        }
//    }

    private static void addCastlingMove(Board board, int kingIndex, int rookIndex,
                                        Function<Integer, Integer> kingMoveModifier,
                                        Function<Integer, Integer> rookMoveModifier,
                                        List<Move> possibleMoves, boolean isCallerWhite) {
        Optional<Piece> maybePiece = board.findPieceByIndex(rookIndex);
        if (maybePiece.isPresent() && maybePiece.get() instanceof Rook &&
                maybePiece.get().isWhite() == isCallerWhite && !((Rook) maybePiece.get()).hasMoved()) {

            int startOfSpaceBetween = rookMoveModifier.apply(rookIndex);
            int endOfSpaceBetween = rookMoveModifier.apply(kingIndex);
            if(startOfSpaceBetween > endOfSpaceBetween){
                //swap values
                int temp = startOfSpaceBetween;
                startOfSpaceBetween = endOfSpaceBetween;
                endOfSpaceBetween = temp;
            }
            if(IntStream.range(startOfSpaceBetween, endOfSpaceBetween)
                    .anyMatch(index -> isBlockedByPiece(board, index) ||
                            isMovingThroughCheckOrIsInCheck(board, index, isCallerWhite))){
                return;
            }

            int destinationIndexForKing = kingMoveModifier.andThen(kingMoveModifier).apply(kingIndex);
            int destinationIndexForRook = rookMoveModifier.apply(destinationIndexForKing);
            possibleMoves.add(new Move(kingIndex, destinationIndexForKing, rookIndex, destinationIndexForRook));
        }
    }

    private static boolean isBlockedByPiece(Board board, int index) {
        return board.findPieceByIndex(index).isPresent();
    }

    private static boolean isMovingThroughCheckOrIsInCheck(Board board, int index, boolean isCallerWhite) {
        return board.getOpponentPieceLocations(isCallerWhite).stream()
                .anyMatch(opponentIndex -> board.getPieceByIndex(opponentIndex).canSeeOpponentPiece(opponentIndex, index, board));
    }

    public static boolean canSeeIndexForKing(int currentIndex, int indexToFind, Board board) {

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
    public static List<Move> getPossibleMovesForPawn(int currentIndex, Board board, boolean isWhite) {

        List<Move> possibleMoves = new ArrayList<>();
        Function<Integer, Integer> moveModifierForward;
        List<Integer> startingRow;
        List<Integer> endRow;

        if (isWhite) {
            moveModifierForward = MOVE_MODIFIER_DOWN;
            startingRow = ROW_7;
            endRow = ROW_1;
        } else {
            moveModifierForward = MOVE_MODIFIER_UP;
            startingRow = ROW_2;
            endRow = ROW_8;
        }

        if (endRow.contains(currentIndex)) {
            throw new RuntimeException("im a queen now this should never happen");
        }

        int forwardMove = moveModifierForward.apply(currentIndex);
        if (board.findPieceByIndex(forwardMove).isEmpty()) {
            possibleMoves.add(new Move(currentIndex, forwardMove));
        }

        //Starting square double move
        if (startingRow.contains(currentIndex)) {
            int doubleForwardMove = moveModifierForward.apply(forwardMove);
            if (board.findPieceByIndex(doubleForwardMove).isEmpty()) {
                possibleMoves.add(new Move(currentIndex, doubleForwardMove));
            }
        }

        if (!COL_A.contains(currentIndex)) {
            int leftDiagonalTake = MOVE_MODIFIER_LEFT.andThen(moveModifierForward).apply(currentIndex);
            addMoveIfEmptyOrOpponent(currentIndex, board, leftDiagonalTake, possibleMoves);
        }
        if (!COL_H.contains(currentIndex)) {
            int rightDiagonalTake = MOVE_MODIFIER_RIGHT.andThen(moveModifierForward).apply(currentIndex);
            addMoveIfEmptyOrOpponent(currentIndex, board, rightDiagonalTake, possibleMoves);
        }

        return possibleMoves;
    }

    //TODO need to check if we are in check by moving this piece, could do this here or tie it with the scoring as we are already checking all positions there.
    // Then remove the node from the tree if we are moving into check. Although this may be bad if we are working from the bottom up. As we may be checking all
    // the nodes below first. Something to look into.
    private static void addMoveIfEmptyOrOpponent(int currentIndex, Board board, int moveIndex, List<Move> possibleMoves) {
        if (board.findPieceByIndex(moveIndex).isPresent() && (board.findPieceByIndex(moveIndex).get().isWhite() != board.getPieceByIndex(currentIndex).isWhite())) {
            possibleMoves.add(new Move(currentIndex, moveIndex));
        }
    }


    //Could continue to use sets here or could use a map of index to piece, might be quicker look up?
    public static boolean canSeeIndexForPawn(int currentIndex, int indexToFind, Board board, boolean isWhite) {

        Function<Integer, Integer> moveModifierForward;
        List<Integer> endRow;

        if (isWhite) {
            moveModifierForward = MOVE_MODIFIER_DOWN;
            endRow = ROW_1;
        } else {
            moveModifierForward = MOVE_MODIFIER_UP;
            endRow = ROW_8;
        }

        if (endRow.contains(currentIndex)) {
            throw new RuntimeException("im a queen now this should never happen");
        }

        if (!COL_A.contains(currentIndex)) {
            int leftDiagonalTake = MOVE_MODIFIER_LEFT.andThen(moveModifierForward).apply(currentIndex);
            if (leftDiagonalTake == indexToFind) {
                return true;
            }
        }
        if (!COL_H.contains(currentIndex)) {
            int rightDiagonalTake = MOVE_MODIFIER_RIGHT.andThen(moveModifierForward).apply(currentIndex);
            return rightDiagonalTake == indexToFind;
        }

        return false;
    }

    public static boolean canSeeIndexForKnight(int currentIndex, int indexToFind, Board board) {

        if (!COL_B.contains(currentIndex)) {
            if (IS_NOT_LAST_ROW.test(currentIndex)) {
                int moveIndex = currentIndex + 6;
                if (moveIndex == indexToFind) {
                    return true;
                }
            }
            if (IS_NOT_FIRST_ROW.test(currentIndex)) {
                int moveIndex = currentIndex - 10;
                if (moveIndex == indexToFind) {
                    return true;
                }
            }

        }
        if (!COL_G.contains(currentIndex)) {
            if (IS_NOT_LAST_ROW.test(currentIndex)) {
                int moveIndex = currentIndex + 10;
                if (moveIndex == indexToFind) {
                    return true;
                }
            }
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
                return moveIndex == indexToFind;
            }
        }

        return false;
    }

    public static List<Move> getPossibleMovesForKnight(int currentIndex, Board board) {

        List<Move> possibleMoves = new ArrayList<>();

        if (!COL_B.contains(currentIndex) && !COL_A.contains(currentIndex)) {
            if (IS_NOT_LAST_ROW.test(currentIndex)) {
                int moveIndex = currentIndex + 6;
                addMoveIfEmptyOrOpponent(currentIndex, board, moveIndex, possibleMoves);
            }
            if (IS_NOT_FIRST_ROW.test(currentIndex)) {
                int moveIndex = currentIndex - 10;
                addMoveIfEmptyOrOpponent(currentIndex, board, moveIndex, possibleMoves);
            }
        }
        if (!COL_G.contains(currentIndex) && !COL_H.contains(currentIndex)) {
            if (IS_NOT_LAST_ROW.test(currentIndex)) {
                int moveIndex = currentIndex + 10;
                addMoveIfEmptyOrOpponent(currentIndex, board, moveIndex, possibleMoves);
            }
            if (IS_NOT_FIRST_ROW.test(currentIndex)) {
                int moveIndex = currentIndex - 6;
                addMoveIfEmptyOrOpponent(currentIndex, board, moveIndex, possibleMoves);
            }
        }
        if (!ROW_2.contains(currentIndex) && !ROW_1.contains(currentIndex)) {
            if (IS_NOT_LAST_COL.test(currentIndex)) {
                int moveIndex = currentIndex - 15;
                addMoveIfEmptyOrOpponent(currentIndex, board, moveIndex, possibleMoves);
            }
            if (IS_NOT_FIRST_COL.test(currentIndex)) {
                int moveIndex = currentIndex - 17;
                addMoveIfEmptyOrOpponent(currentIndex, board, moveIndex, possibleMoves);
            }
        }
        if (!ROW_7.contains(currentIndex) && !ROW_8.contains(currentIndex)) {
            if (IS_NOT_LAST_COL.test(currentIndex)) {
                int moveIndex = currentIndex + 17;
                addMoveIfEmptyOrOpponent(currentIndex, board, moveIndex, possibleMoves);
            }
            if (IS_NOT_FIRST_COL.test(currentIndex)) {
                int moveIndex = currentIndex + 15;
                addMoveIfEmptyOrOpponent(currentIndex, board, moveIndex, possibleMoves);
            }
        }

        return possibleMoves;
    }

    public static boolean isPawnQueenable(int newIndex, Piece piece) {
        List<Integer> endRow;
        if (piece.isWhite()) {
            endRow = ROW_1;
        } else {
            endRow = ROW_8;
        }
        return endRow.contains(newIndex);
    }
}
