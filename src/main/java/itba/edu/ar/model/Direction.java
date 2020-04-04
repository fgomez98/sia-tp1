package itba.edu.ar.model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum Direction {

    UP(-1, 0), DOWN(1, 0), LEFT(0, -1), RIGHT(0, 1),
    UP_RIGHT(-1, 1), DOWN_RIGHT(1, 1),
    UP_LEFT(-1, -1), DOWN_LEFT(1, -1);

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

    public static List<Direction> surroundings = Arrays.asList(UP, DOWN, LEFT, RIGHT);

    public static Optional<Direction> from(String str) {
        if(str != null ) {
            try {
                return Optional.of(Direction.valueOf(str.trim().toUpperCase()));
            } catch(IllegalArgumentException ex) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }
}
