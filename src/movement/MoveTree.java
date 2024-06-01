package movement;

import java.util.ArrayList;
import java.util.List;

public class MoveTree {

    private List<MoveNode> nodes = new ArrayList<>();

    public List<MoveNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<MoveNode> nodes) {
        this.nodes = nodes;
    }
}
