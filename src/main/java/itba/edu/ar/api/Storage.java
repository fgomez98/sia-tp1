package itba.edu.ar.api;

import itba.edu.ar.ai.Node;

public interface Storage {

    void add(Node node);

    Node get();

    boolean isEmpty();

    int frontierSize();

    int exploredSize();
}
