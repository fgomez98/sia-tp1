package itba.edu.ar.models;

public class Tile {

    private final int x;
    private final int y;
    private Entity entity; // Entidad que almacenan dentro del casillero si es que pueden

    Tile(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public static Tile get(final int x, final int y) {
        return new Tile(x, y);
    }

    @Override
    public String toString() {
        return " ";
    }

    public boolean canOccupy() {
        return true;
    }

    static Tile get(char c, int x, int y) {
        switch (c) {
            case '.':
                return BoxSpot.get(x, y);
            case '@':
                Tile t = Tile.get(x, y);
                Entity e = Player.get(t);
                return Player.get();
            case '$':
                return Box.get(x, y);
            case '#':
                return Wall.get(x, y);
            default:
                return Tile.get(x, y);
        }
    }
}
