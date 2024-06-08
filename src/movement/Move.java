package movement;

import util.Pair;

import java.util.Optional;

public class Move {
    private Pair<Integer, Integer> move;

    /**
     *  Castling is the only time that two moves are required, one for the king's move and one for the rook's move, hence the optional
     */
    private Optional<Pair<Integer, Integer>> castleMove = Optional.empty();

    public Move(Integer currentIndex, Integer destinationIndex) {
        move = new Pair<>(currentIndex, destinationIndex);
    }

    public Move(Integer currentIndexForKing, Integer destinationIndexForKing, Integer currentIndexForRook, Integer destinationIndexForRook ) {
        move = new Pair<>(currentIndexForKing, destinationIndexForKing);
        castleMove = Optional.of(new Pair<>(currentIndexForRook, destinationIndexForRook));
    }

    public Pair<Integer, Integer> getMove() {
        return move;
    }

    public Optional<Pair<Integer, Integer>> getCastleMove() {
        return castleMove;
    }
}
