package movement;

import util.Pair;

import java.util.Optional;

public class Move {
    private Pair<Integer, Integer> move;

    /**
     *  Castling is the only time that two moves are required, one for the king's move and one for the rook's move, hence the optional
     */
    private Optional<Pair<Integer, Integer>> castleMove;

    public Move(Integer left, Integer right) {

    }

    public Pair<Integer, Integer> getMove() {
        return move;
    }

    public Optional<Pair<Integer, Integer>> getCastleMove() {
        return castleMove;
    }
}
