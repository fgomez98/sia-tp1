package itba.edu.ar.api;

import itba.edu.ar.ai.Node;

public interface Storage {

    void push(Node node);

    Node pop();

    boolean isEmpty();
}
