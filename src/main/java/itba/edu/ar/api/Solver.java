package itba.edu.ar.api;

import itba.edu.ar.ai.Node;

import java.io.IOException;
import java.util.Optional;

public interface Solver {

    Optional<Node> solve();

    void outputMovments(Node node, String fileName) throws IOException;

}
