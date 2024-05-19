package piece;

import board.Board;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class MoveUtil {

    private static final List<Integer> COL_A = Arrays.asList(0, 8, 16, 24, 32, 40, 48, 56);

    private static final List<Integer> COL_H = Arrays.asList(7, 15, 23, 31, 39, 47, 55, 63);

    private static final List<Integer> ROW_1 = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);

    private static final List<Integer> ROW_8 = Arrays.asList(56, 57, 58, 59, 60, 61, 62, 63);

    private static final Predicate<Integer> IS_NOT_FIRST_COL = index -> !COL_A.contains(index);

    private static final Predicate<Integer> IS_NOT_LAST_COL = index -> !COL_H.contains(index);

    private static final Predicate<Integer> IS_NOT_FIRST_ROW = index -> !ROW_1.contains(index);

    private static final Predicate<Integer> IS_NOT_LAST_ROW = index -> !ROW_8.contains(index);

    private static final Function<Integer, Integer> MOVE_MODIFIER_RIGHT = index -> index + 1;

    private static final Function<Integer, Integer> MOVE_MODIFIER_LEFT = index -> index - 1;

    private static final Function<Integer, Integer> MOVE_MODIFIER_UP = index -> index + 8;

    private static final Function<Integer, Integer> MOVE_MODIFIER_DOWN = index -> index - 8;

    public static Map<Integer, Integer> getPossibleMovesForSlidingPiece(int position, Board board, SlidingType type) {
        Map<Integer, Integer> possibleMoves = new HashMap<>();

        if (type == SlidingType.STRAIGHT || type == SlidingType.BOTH) {
            possibleMoves.putAll(getPossibleMovesForDirection(position, board, IS_NOT_LAST_COL, MOVE_MODIFIER_RIGHT));
            possibleMoves.putAll(getPossibleMovesForDirection(position, board, IS_NOT_FIRST_COL, MOVE_MODIFIER_LEFT));
            possibleMoves.putAll(getPossibleMovesForDirection(position, board, IS_NOT_LAST_ROW, MOVE_MODIFIER_UP));
            possibleMoves.putAll(getPossibleMovesForDirection(position, board, IS_NOT_FIRST_ROW, MOVE_MODIFIER_DOWN));
        }
        if (type == SlidingType.DIAGONAL || type == SlidingType.BOTH) {
            possibleMoves.putAll(getPossibleMovesForDirection(position, board, IS_NOT_LAST_ROW.and(IS_NOT_LAST_COL), MOVE_MODIFIER_UP.andThen(MOVE_MODIFIER_RIGHT)));
            possibleMoves.putAll(getPossibleMovesForDirection(position, board, IS_NOT_LAST_ROW.and(IS_NOT_FIRST_COL), MOVE_MODIFIER_UP.andThen(MOVE_MODIFIER_LEFT)));
            possibleMoves.putAll(getPossibleMovesForDirection(position, board, IS_NOT_FIRST_ROW.and(IS_NOT_LAST_COL), MOVE_MODIFIER_DOWN.andThen(MOVE_MODIFIER_RIGHT)));
            possibleMoves.putAll(getPossibleMovesForDirection(position, board, IS_NOT_FIRST_ROW.and(IS_NOT_FIRST_COL), MOVE_MODIFIER_DOWN.andThen(MOVE_MODIFIER_LEFT)));
        }

        return possibleMoves;
    }

    private static Map<Integer, Integer> getPossibleMovesForDirection(int currentIndex, Board board, Predicate<Integer> isNotEndOfSearch, Function<Integer, Integer> moveModifier) {
        Map<Integer, Integer> possibleMoves = new HashMap<>();

        int previousIndex;
        previousIndex = currentIndex;
        while (isNotEndOfSearch.test(previousIndex)) {
            previousIndex = moveModifier.apply(previousIndex);
            Optional<Piece> maybePiece = board.getTileByIndex(previousIndex);

            if (maybePiece.isPresent()) {
                //Add move if we can take the piece
                if (!maybePiece.get().isFriendly()) {
                    possibleMoves.put(previousIndex, maybePiece.get().getScore());
                }
                //Don't look any further down this line as we are blocked by an opponent or friendly piece.
                break;
            } else {
                possibleMoves.put(previousIndex, Board.EMPTY_TILE_SCORE);
            }
        }

        return possibleMoves;
    }
}
