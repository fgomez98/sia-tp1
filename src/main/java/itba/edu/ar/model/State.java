package itba.edu.ar.model;

import java.util.HashSet;
import java.util.Set;

public class State {

    private final Set<Coordinate> boxes;
    private final Coordinate player;

    private State(Set<Coordinate> boxes, Coordinate player) {
        this.boxes = boxes;
        this.player = player;
    }

    static State from(Set<Coordinate> boxes, Coordinate player) {
        return new State(new HashSet<>(boxes), player);
    }

    static State from(State state) {
        return new State(state.getBoxes(), state.getPlayer());
    }

    public Set<Coordinate> getBoxes() {
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
        int result = boxes.hashCode();
        result = 31 * result + player.hashCode();
        return result;
    }
}
