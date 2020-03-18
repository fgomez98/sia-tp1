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
        return new State(new LinkedList<>(boxes), player);
    }

    public List<Coordinate> getBoxes() {
        return boxes;
    }

    public Coordinate getPlayer() {
        return player;
    }
}
