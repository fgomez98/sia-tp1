package itba.edu.ar.model;

import java.util.Arrays;
import java.util.List;

public enum Direction {

    UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1);

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
