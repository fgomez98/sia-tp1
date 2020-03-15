package itba.edu.ar.models;

public class Box extends Entity {

    private Box(Tile tile) {
        super(tile);
    }

    public static Box get(Tile tile) {
        return new Box(tile);
    }

    @Override
    public String toString() {
        return "$";
    }

}
