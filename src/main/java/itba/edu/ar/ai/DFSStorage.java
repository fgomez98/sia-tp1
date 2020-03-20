package itba.edu.ar.ai;

import itba.edu.ar.api.Storage;

import java.util.Stack;

public class DFSStorage implements Storage {

    private Stack<Node> stack;

    private DFSStorage() {
        stack = new Stack<>();
    }

    public static DFSStorage getStorage() {
        return new DFSStorage();
    }

    @Override
    public void add(Node node) {
        stack.push(node);
    }

    @Override
    public Node get() {
        return stack.pop();
    }

    @Override
    public boolean isEmpty() {
        return stack.empty();
    }
}
