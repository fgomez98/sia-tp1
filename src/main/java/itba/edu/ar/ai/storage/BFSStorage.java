package itba.edu.ar.ai.storage;

import itba.edu.ar.ai.Benchmarking;
import itba.edu.ar.ai.Node;
import itba.edu.ar.api.Storage;
import itba.edu.ar.model.State;

import java.util.*;

public class BFSStorage implements Storage {

    private Queue<Node> queue;
    private Set<State> explored;

    private BFSStorage() {
        queue = new LinkedList<>();
        explored = new HashSet<>();
    }

    public static BFSStorage getStorage() {
        return new BFSStorage();
    }

    @Override
    public void add(Node node) {
        if (!explored.contains(node.getState())) {
            Benchmarking.nodesFrontier++;
            queue.add(node);
        }
    }

    @Override
    public Node get() {
        Benchmarking.nodesFrontier--;
        Node node = queue.poll();
        explored.add(node.getState());
        return node;
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
