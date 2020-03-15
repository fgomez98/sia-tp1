package itba.edu.ar.models;

public class Player extends Entity {

    private Player(Tile tile) {
        super(tile);
    }

    public static Player get(Tile tile) {
        return new Player(tile);
    }

    @Override
    public String toString() {
        return "@";
    }
}
