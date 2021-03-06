package itba.edu.ar.model;

public class Coordinate {
    private final int x;
    private final int y;

    protected Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Coordinate move(int steps, Direction d) {
        return Coordinate.from(x + steps * d.getX(), y + steps * d.getY());
    }

    Coordinate move(Direction d) {
        return move(1, d);
    }

    static Coordinate from(int x, int y) {
        return new Coordinate(x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinate that = (Coordinate) o;

        if (x != that.x) return false;
        return y == that.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

}
