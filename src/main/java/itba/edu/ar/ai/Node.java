package itba.edu.ar.ai;

import itba.edu.ar.model.Direction;
import itba.edu.ar.model.State;
import java.util.LinkedList;
import java.util.Queue;

public class Node {

    private State state;
    private Queue<Direction> movements;
    private int depth;
    private int eval;
    private int cost;

    public Queue<Direction> getMovements() {
        return movements;
    }

    public int getDepth() {
        return depth;
    }

    public State getState() {
        return state;
    }

    public int getEval() {
        return eval;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (depth != node.depth) return false;
        if (state != null ? !state.equals(node.state) : node.state != null) return false;
        return movements != null ? movements.equals(node.movements) : node.movements == null;
    }

    @Override
    public int hashCode() {
        return state != null ? state.hashCode() : 0;
    }

    public static class Builder {
        private State state;
        private int depth;
        private Queue<Direction> movements;
        private int eval;
        private int cost;

        public Builder(State state) {
            this.state = state;
            this.depth = 0;
            this.movements = new LinkedList<>();
        }

        public Builder withParent(Node parent) {
            this.depth = parent.getDepth() + 1;
            this.movements = new LinkedList<>(parent.getMovements());
            return this;
        }

        public Builder withMovement(Direction direction) {
            this.movements.offer(direction);
            return this;
        }

        public Builder withEvaluation(int eval) {
            this.eval = eval;
            return this;
        }

        public Builder withCost(int cost) {
            this.cost = cost;
            return this;
        }

        public Node build() {
            return new Node(this);
        }
    }

    private Node(Builder builder) {
        this.state = builder.state;
        this.movements = builder.movements;
        this.depth = builder.depth;
        this.eval = builder.eval;
        this.cost = builder.cost;
    }

}
