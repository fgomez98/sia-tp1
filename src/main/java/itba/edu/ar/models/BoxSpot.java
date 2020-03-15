package itba.edu.ar.models;

public class BoxSpot extends Tile {

    private BoxSpot(final int x, final int y) {
        super(x, y);
    }

    public static BoxSpot get(final int x, final int y) {
        return new BoxSpot(x, y);
    }

    @Override
    public String toString() {
        return isOccupied() ? this.getEntity().toString() : ".";
    }
}
