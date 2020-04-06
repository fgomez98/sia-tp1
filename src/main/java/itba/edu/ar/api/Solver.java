package itba.edu.ar.api;

import itba.edu.ar.ai.Node;
import itba.edu.ar.model.Either;

import java.io.IOException;
import java.util.Optional;

public interface Solver {

    Either<Node,Boolean> solve();

    void outputMovments(Node node, String fileName) throws IOException;

}
