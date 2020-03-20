package itba.edu.ar.ai;


import itba.edu.ar.api.Storage;

import java.util.LinkedList;
import java.util.Queue;

public class BFSStorage implements Storage {

    private Queue<Node> queue;

    private BFSStorage() {
        queue = new LinkedList<>();
    }

    public static BFSStorage getStorage() {
        return new BFSStorage();
    }

    @Override
    public void add(Node node) {
        queue.add(node);
    }

    @Override
    public Node get() {
        return queue.poll();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
