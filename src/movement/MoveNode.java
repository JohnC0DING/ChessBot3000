package movement;

import board.Board;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MoveNode {

    private final Move move;

    private final Optional<MoveNode> parent;

    private final Optional<Board> boardSnapshot;

    private Optional<Integer> score = Optional.empty();

    private List<MoveNode> children = Collections.emptyList();

    public MoveNode(Move move, MoveNode parent) {
        this.move = move;
        this.parent = Optional.ofNullable(parent);
        this.boardSnapshot = Optional.empty();
    }

    public MoveNode(Move move, MoveNode parent, Board boardSnapshot) {
        this.move = move;
        this.parent = Optional.ofNullable(parent);
        this.boardSnapshot = Optional.ofNullable(boardSnapshot);
    }

    public MoveNode(Move move) {
        this.move = move;
        this.boardSnapshot = Optional.empty();
        this.parent = Optional.empty();
    }

    public boolean hasChildren(){
        return !children.isEmpty();
    }

    public Optional<MoveNode> getParent() {
        return parent;
    }

    public List<MoveNode> getChildren() {
        return children;
    }

    public Move getMove() {
        return move;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Optional<Integer> score) {
        this.score = score;
    }

    public Optional<Board> getBoardSnapshot() {
        return boardSnapshot;
    }
}
