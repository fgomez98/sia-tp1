package itba.edu.ar.ai;

import itba.edu.ar.api.Storage;

import java.util.Comparator;
import java.util.Set;

public class GlobalGreedy implements Storage {

    private Set<Node> set;
    private Comparator<Node> comparator;

    private GlobalGreedy() {
    }

    public static DFSStorage get() {
        return null;
    }

    @Override
    public void push(Node node) {

    }

    @Override
    public Node pop() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
