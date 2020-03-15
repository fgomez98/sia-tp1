package itba.edu.ar.models;

public class Wall extends Tile {

    private Wall(final int x, final int y) {
        super(x, y);
    }

    public static Wall get(final int x, final int y) {
        return new Wall(x, y);
    }

    @Override
    public boolean canOccupy() {
        return false;
    }

    @Override
    public String toString() {
        return "#";
    }
}
