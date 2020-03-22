package itba.edu.ar.ai;

import itba.edu.ar.model.Direction;
import itba.edu.ar.model.State;

import java.util.Queue;

public class InformedNode extends Node {

    private int evaluation;

    public InformedNode(State state, Queue<Direction> movements, int depth, Node parent, int evaluation) {
        super(state, movements, depth, parent);
        this.evaluation = evaluation;
    }

    public InformedNode(State state, int depth, Node parent, int evaluation) {
        super(state, depth, parent);
        this.evaluation = evaluation;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }
}
