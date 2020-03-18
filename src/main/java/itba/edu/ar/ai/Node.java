package itba.edu.ar.ai;

import itba.edu.ar.model.Direction;
import itba.edu.ar.model.State;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Node {

    private State state;
    private Queue<Direction> movements; /* Si solo se va a usar para imprmir las acciones ralizadas por que no usar String Builder? */
    private Set<Edge> children;
    private final int depth;
    Node parent;

    public Node(State state, Queue<Direction> movements, int depth) {
        this(state, depth);
        this.movements = movements;
    }

    public Node(State state, int depth) {
        this.depth = depth;
        this.state = state;
        this.children = new HashSet<>();
        this.movements = new LinkedList<>();
    }

    public void addChild(Node node, int edgeCost) {
        children.add(new Edge(edgeCost, node));
    }

    public void addChild(Node node) {
        children.add(new Edge(node));
    }

    public Set<Edge> getChildren() {
        return children;
    }

    public Queue<Direction> getMovements() {
        return movements;
    }

    public void addMovement(Direction direction){
        movements.add(direction);
    }

    public int getDepth() {
        return depth;
    }

    public State getState() {
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (depth != node.depth) return false;
        if (state != null ? !state.equals(node.state) : node.state != null) return false;
        if (movements != null ? !movements.equals(node.movements) : node.movements != null) return false;
        return children != null ? children.equals(node.children) : node.children == null;
    }

    @Override
    public int hashCode() {
        return state != null ? state.hashCode() : 0;
    }

    private class Edge {
        private int cost;
        private Node adjecent;

        private Edge(int cost, Node adjecent) {
            this.cost = cost;
            this.adjecent = adjecent;
        }

        private Edge(Node adjecent) {
            this.cost = 0; /* O 1 tmb podria*/
            this.adjecent = adjecent;
        }

        public int getCost() {
            return cost;
        }

        public Node getAdjecent() {
            return adjecent;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Edge edge = (Edge) o;

            if (cost != edge.cost) return false;
            return adjecent.equals(edge.adjecent);
        }

        @Override
        public int hashCode() {
            int result = cost;
            result = 31 * result + adjecent.hashCode();
            return result;
        }
    }
}
