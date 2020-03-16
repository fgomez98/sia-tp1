package itba.edu.ar.models;

public class Coordinate {
    private int x;
    private int y;

    private Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate move(int steps, Direction d) {
        return Coordinate.from(x + steps * d.getX(), y + steps * d.getY());
    }

    public Coordinate move(Direction d) {
        return move(1, d);
    }

    public static Coordinate from(int x, int y) {
        return new Coordinate(x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
