package itba.edu.ar.api;

import itba.edu.ar.model.Board;
import itba.edu.ar.model.State;

public interface Heuristic {

    int evaluate(Board board, State state);

}
