package movement;

import board.Board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MoveNode {

    private final Move move;

    private final Optional<MoveNode> parent;

    private final Board boardSnapshot;

    private Integer score;

    private final List<MoveNode> children = new ArrayList<>();

    public MoveNode(Move move, MoveNode parent, Board boardSnapshot) {
        this.move = move;
        this.parent = Optional.ofNullable(parent);
        this.boardSnapshot = boardSnapshot;
    }

    public MoveNode(Move move, Board boardSnapshot) {
        this.move = move;
        this.parent = Optional.empty();
        this.boardSnapshot = boardSnapshot;
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

    public void setScore(Integer score) {
        this.score = score;
    }

    public Board getBoardSnapshot() {
        return boardSnapshot;
    }
}
