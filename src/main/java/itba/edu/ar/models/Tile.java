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

    void setEntity(Entity entity) {
        this.entity = entity;
    }

    public static Tile get(final int x, final int y) {
        return new Tile(x, y);
    }

    public boolean isOccupied() {
        return entity != null;
    }

    @Override
    public String toString() {
        return isOccupied() ? entity.toString() : " ";
    }

    public boolean canOccupy() {
        return true;
    }

    public static Tile get(char c, int x, int y) {
        switch (c) {
            case '.':
                return BoxSpot.get(x, y);
            case '@':
                Tile tp = Tile.get(x, y);
                Entity ep = Player.get(tp);
                tp.setEntity(ep);
                return tp;
            case '$':
                Tile tb = Tile.get(x, y);
                Entity eb = Box.get(tb);
                tb.setEntity(eb);
                return tb;
            case '#':
                return Wall.get(x, y);
            default:
                return Tile.get(x, y);
        }
    }
}
