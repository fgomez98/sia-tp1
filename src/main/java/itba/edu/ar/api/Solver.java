package itba.edu.ar.api;

import itba.edu.ar.ai.Node;
import itba.edu.ar.model.Either;

import java.io.IOException;

public interface Solver {

    Either<Node, Boolean> solve();

    void outputMovements(Either<Node, Boolean> node, String fileName) throws IOException;

}
