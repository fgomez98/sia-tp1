package itba.edu.ar.ai;

import itba.edu.ar.api.Storage;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

public class HPAGreedy implements Storage {

    private SortedSet<Node> set;

    private HPAGreedy(double w) {
        Comparator<Node> comparator = (n1, n2) -> Double.compare(fn(n1, w), fn(n2, w));
        set = new TreeSet<>();
    }

    private double fn(Node node, double w) {
        return (1 - w) * Cost.getCost(node) + w * ((InformedNode) node).getEvaluation();
    }

    public static HPAGreedy getStorage(double w) {
        return new HPAGreedy(w);
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
