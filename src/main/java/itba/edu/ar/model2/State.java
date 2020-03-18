package itba.edu.ar.model2;

import itba.edu.ar.model.Coordinate;

import java.util.LinkedList;
import java.util.List;

public class State {

    private final List<Coordinate> boxes;
    private final Coordinate player;

    private State(List<Coordinate> boxes, Coordinate player) {
        this.boxes = boxes;
        this.player = player;
    }

    static State from(List<Coordinate> boxes, Coordinate player) {
        return new State(boxes, player);
    }

    public List<Coordinate> getBoxes() {
        return boxes;
    }

    public Coordinate getPlayer() {
        return player;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state = (State) o;

        if (!boxes.equals(state.boxes)) return false;
        return player.equals(state.player);
    }

    @Override
    public int hashCode() {
        int result = player.hashCode();
        for (Coordinate coord: boxes) {
            result += 31 * result + coord.hashCode();
        }
        return result;
    }
}
