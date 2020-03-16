package itba.edu.ar.models;

public class Goal extends Tile {

    private Goal(final int x, final int y) {
        super(x, y);
    }

    public static Goal get(final int x, final int y) {
        return new Goal(x, y);
    }

    @Override
    public String toString() {
        return isFree() ? this.getEntity().toString() : ".";
    }
}
