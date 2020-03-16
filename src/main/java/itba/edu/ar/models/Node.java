package itba.edu.ar.models;


import java.util.List;
import java.util.Objects;

public class Node {

    private Coordinate playerPosition;
    private List<Coordinate> boxPosition;
    private List<Directions> movements;

    public Node(Coordinate playerPosition, List<Coordinate> boxPosition, List<Directions> movements) {
        this.playerPosition = playerPosition;
        this.boxPosition = boxPosition;
        this.movements = movements;
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

    public List<Directions> getMovements() {
        return movements;
    }

    public void setMovements(List<Directions> movements) {
        this.movements = movements;
    }

    public void addMovement(Directions directions){
        movements.add(directions);
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
}
