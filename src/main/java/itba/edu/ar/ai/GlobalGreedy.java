package itba.edu.ar.ai;

import itba.edu.ar.api.Storage;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class GlobalGreedy implements Storage {

    private SortedSet<Node> set;

    private GlobalGreedy() {
        Comparator<Node> comparator = (n1, n2) -> Integer.compare(((InformedNode)n1).getEvaluation(), ((InformedNode)n2).getEvaluation());
        set = new TreeSet<>(comparator);
    }

    public static DFSStorage getStorage() {
        return null;
    }

    @Override
    public Node get() {
        return set.first();
    }

    @Override
    public void add(Node node) {
        set.add(node);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
