package itba.edu.ar.ai.storage;

import itba.edu.ar.ai.Node;
import itba.edu.ar.api.Storage;
import itba.edu.ar.model.State;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class DFSStorage implements Storage {

    private Stack<Node> stack;
    private Set<State> explored;

    private DFSStorage() {
        stack = new Stack<>();
        explored = new HashSet<>();
    }

    public static DFSStorage getStorage() {
        return new DFSStorage();
    }

    @Override
    public void add(Node node) {
        if (!explored.contains(node.getState())) {
            stack.push(node);
        }
    }

    @Override
    public Node get() {
        Node node = stack.pop();
        explored.add(node.getState());
        return node;
    }

    @Override
    public boolean isEmpty() {
        return stack.empty();
    }

    @Override
    public int frontierSize() {
        return stack.size();
    }

    @Override
    public int exploredSize() {
        return explored.size();
    }
}
