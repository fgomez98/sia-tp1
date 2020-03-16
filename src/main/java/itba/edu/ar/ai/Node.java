package itba.edu.ar.ai;

import itba.edu.ar.models.Coordinate;
import itba.edu.ar.models.Direction;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Node {

    private Coordinate playerPosition;
    private List<Coordinate> boxPosition;
    private List<Direction> movements;
    private Set<Edge> childs;

    public Node(Coordinate playerPosition, List<Coordinate> boxPosition, List<Direction> movements) {
        this.playerPosition = playerPosition;
        this.boxPosition = boxPosition;
        this.movements = movements;
    }

    public void addChild(Node node, int edgeCost) {
        childs.add(new Edge(edgeCost, node));
    }

    public void addChild(Node node) {
        childs.add(new Edge(node));
    }

    public Set<Edge> getChilds() {
        return childs;
    }

    public Coordinate getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(Coordinate playerPosition) {
        this.playerPosition = playerPosition;
    }

    public List<Coordinate> getBoxPosition() {
        return boxPosition;
    }

    public void setBoxPosition(List<Coordinate> boxPosition) {
        this.boxPosition = boxPosition;
    }

    public List<Direction> getMovements() {
        return movements;
    }

    public void setMovements(List<Direction> movements) {
        this.movements = movements;
    }

    public void addMovement(Direction direction){
        movements.add(direction);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(playerPosition, node.playerPosition) &&
                Objects.equals(boxPosition, node.boxPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerPosition, boxPosition);
    }

    public class Edge {
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
