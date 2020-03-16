package itba.edu.ar.model;

import java.util.Arrays;
import java.util.List;

public enum Direction {

    UP(0, 1), DOWN(0, -1), LEFT(-1, 0), RIGHT(1, 0);

    private final int x;
    private final int y;

    Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static List<Direction> directions = Arrays.asList(UP, DOWN, LEFT, RIGHT);
}
