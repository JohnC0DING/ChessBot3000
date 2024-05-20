package piece;

import board.Board;
import util.Pair;

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

    public static Map<Pair<Integer, Integer>, Integer> getPossibleMovesForSlidingPiece(int position, Board board, SlidingType type) {
        Map<Pair<Integer, Integer>, Integer> possibleMoves = new HashMap<>();
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

    public static Map<Pair<Integer, Integer>, Integer> getPossibleMovesForDirection(int currentIndex, Board board, Predicate<Integer> isNotEndOfSearch, Function<Integer, Integer> moveModifier) {
        Map<Pair<Integer, Integer>, Integer> possibleMoves = new HashMap<>();

        int moveIndex = currentIndex;
        while (isNotEndOfSearch.test(moveIndex)) {
            moveIndex = moveModifier.apply(moveIndex);
            Optional<Piece> maybePiece = board.getTileByIndex(moveIndex);

            if (maybePiece.isPresent()) {
                //Add move if we can take the piece
                if (!maybePiece.get().isFriendly()) {
                    possibleMoves.put(new Pair<>(currentIndex, moveIndex), maybePiece.get().getScore());
                }
                //Don't look any further down this line as we are blocked by an opponent or friendly piece.
                break;
            } else {
                possibleMoves.put(new Pair<>(currentIndex, moveIndex), Board.EMPTY_TILE_SCORE);
            }
        }

        return possibleMoves;
    }

    public static Map<Pair<Integer, Integer>, Integer> getPossibleMovesForKing(int currentIndex, Board board){
        Map<Pair<Integer, Integer>, Integer> possibleMoves = new HashMap<>();

        if(IS_NOT_FIRST_COL.test(currentIndex)){
            int moveIndex = MOVE_MODIFIER_LEFT.apply(currentIndex);
            possibleMoves.put(new Pair<>(currentIndex, moveIndex), getScoreForMove(board, moveIndex));
        }
        if(IS_NOT_LAST_COL.test(currentIndex)){
            int moveIndex = MOVE_MODIFIER_RIGHT.apply(currentIndex);
            possibleMoves.put(new Pair<>(currentIndex, moveIndex), getScoreForMove(board, moveIndex));
        }
        if(IS_NOT_FIRST_ROW.test(currentIndex)){
            int moveIndex = MOVE_MODIFIER_DOWN.apply(currentIndex);
            possibleMoves.put(new Pair<>(currentIndex, moveIndex), getScoreForMove(board, moveIndex));
        }
        if(IS_NOT_LAST_ROW.test(currentIndex)){
            int moveIndex = MOVE_MODIFIER_UP.apply(currentIndex);
            possibleMoves.put(new Pair<>(currentIndex, moveIndex), getScoreForMove(board, moveIndex));
        }
        if(IS_NOT_FIRST_COL.and(IS_NOT_FIRST_ROW).test(currentIndex)){
            int moveIndex = MOVE_MODIFIER_LEFT.andThen(MOVE_MODIFIER_DOWN).apply(currentIndex);
            possibleMoves.put(new Pair<>(currentIndex, moveIndex), getScoreForMove(board, moveIndex));
        }
        if(IS_NOT_FIRST_COL.and(IS_NOT_LAST_ROW).test(currentIndex)){
            int moveIndex = MOVE_MODIFIER_LEFT.andThen(MOVE_MODIFIER_UP).apply(currentIndex);
            possibleMoves.put(new Pair<>(currentIndex, moveIndex), getScoreForMove(board, moveIndex));
        }
        if(IS_NOT_LAST_COL.and(IS_NOT_FIRST_ROW).test(currentIndex)){
            int moveIndex = MOVE_MODIFIER_RIGHT.andThen(MOVE_MODIFIER_DOWN).apply(currentIndex);
            possibleMoves.put(new Pair<>(currentIndex, moveIndex), getScoreForMove(board, moveIndex));
        }
        if(IS_NOT_LAST_COL.and(IS_NOT_LAST_ROW).test(currentIndex)){
            int moveIndex = MOVE_MODIFIER_RIGHT.andThen(MOVE_MODIFIER_UP).apply(currentIndex);
            possibleMoves.put(new Pair<>(currentIndex, moveIndex), getScoreForMove(board, moveIndex));
        }

        return possibleMoves;
    }

    //Could continue to use sets here or could use a map of index to piece, might be quicker look up?
    public static Map<Pair<Integer, Integer>, Integer> getPossibleMovesForPawn(int currentIndex, Board board, boolean isWhite) {

        Map<Pair<Integer, Integer>, Integer> possibleMoves = new HashMap<>();
        Function<Integer, Integer> moveModifierForward = isWhite ? MOVE_MODIFIER_UP : MOVE_MODIFIER_DOWN;

        if(currentIndex / 56 >= 1){
            //im a queen now this should never happen
            return null;
        }

        int forwardMove = moveModifierForward.apply(currentIndex);
        if(board.getTileByIndex(forwardMove).isEmpty()){
            possibleMoves.put(new Pair<>(currentIndex, forwardMove), Board.EMPTY_TILE_SCORE);
        }
        int leftDiagonalTake = MOVE_MODIFIER_LEFT.andThen(moveModifierForward).apply(currentIndex);
        if(board.getTileByIndex(leftDiagonalTake).isPresent() && board.getTileByIndex(leftDiagonalTake).get().isFriendly()){
            possibleMoves.put(new Pair<>(currentIndex, leftDiagonalTake), getScoreForMove(board, leftDiagonalTake));
        }
        int rightDiagonalTake = MOVE_MODIFIER_RIGHT.andThen(moveModifierForward).apply(currentIndex);
        if(board.getTileByIndex(rightDiagonalTake).isPresent() && board.getTileByIndex(rightDiagonalTake).get().isFriendly()){
            possibleMoves.put(new Pair<>(currentIndex, rightDiagonalTake), getScoreForMove(board, rightDiagonalTake));
        }

        return possibleMoves;
    }

    public static Map<Pair<Integer, Integer>, Integer> getPossibleMovesForKnight(int currentIndex, Board board){
        final List<Integer> COL_B = Arrays.asList(1, 9, 17, 25, 33, 41, 49, 57);
        final List<Integer> COL_G = Arrays.asList(6, 14, 22, 30, 38, 46, 54, 62);
        final List<Integer> ROW_2 = Arrays.asList(8, 9, 10, 11, 12, 13, 14, 15);
        final List<Integer> ROW_7 = Arrays.asList(48, 49, 50, 51, 52, 53, 54, 55);

        Map<Pair<Integer, Integer>, Integer> possibleMoves = new HashMap<>();

        if(!COL_B.contains(currentIndex)){
            if(IS_NOT_LAST_ROW.test(currentIndex)){
                int moveIndex = currentIndex + 6;
                possibleMoves.put(new Pair<>(currentIndex, moveIndex), getScoreForMove(board, moveIndex));
            }
            if(IS_NOT_FIRST_ROW.test(currentIndex)){
                int moveIndex = currentIndex - 10;
                possibleMoves.put(new Pair<>(currentIndex, moveIndex), getScoreForMove(board, moveIndex));
            }

        }
        if(!COL_G.contains(currentIndex)){
            if(IS_NOT_LAST_ROW.test(currentIndex)){
                int moveIndex = currentIndex + 10;
                possibleMoves.put(new Pair<>(currentIndex, moveIndex), getScoreForMove(board, moveIndex));
            }
            if(IS_NOT_FIRST_ROW.test(currentIndex)){
                int moveIndex = currentIndex - 6;
                possibleMoves.put(new Pair<>(currentIndex, moveIndex), getScoreForMove(board, moveIndex));
            }
        }
        if(!ROW_2.contains(currentIndex)){
            if(IS_NOT_LAST_COL.test(currentIndex)){
                int moveIndex = currentIndex - 15;
                possibleMoves.put(new Pair<>(currentIndex, moveIndex), getScoreForMove(board, moveIndex));
            }
            if(IS_NOT_FIRST_COL.test(currentIndex)){
                int moveIndex = currentIndex - 17;
                possibleMoves.put(new Pair<>(currentIndex, moveIndex), getScoreForMove(board, moveIndex));
            }
        }
        if(!ROW_7.contains(currentIndex)){
            if(IS_NOT_LAST_COL.test(currentIndex)){
                int moveIndex = currentIndex + 17;
                possibleMoves.put(new Pair<>(currentIndex, moveIndex), getScoreForMove(board, moveIndex));
            }
            if(IS_NOT_FIRST_COL.test(currentIndex)){
                int moveIndex = currentIndex + 15;
                possibleMoves.put(new Pair<>(currentIndex, moveIndex), getScoreForMove(board, moveIndex));
            }
        }

        return possibleMoves;
    }

    private static Integer getScoreForMove(Board board, int moveIndex) {
        Optional<Piece> maybePiece = board.getTileByIndex(moveIndex);
        return  maybePiece.map(Piece::getScore).orElse(Board.EMPTY_TILE_SCORE);
    }
}
