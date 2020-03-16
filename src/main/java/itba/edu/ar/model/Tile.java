package itba.edu.ar.model;

public class Tile {

    private final Coordinate coordinate;
    private Entity entity; // Entidad que almacenan dentro del casillero si es que pueden

    Tile(final int x, final int y) {
        this.coordinate = Coordinate.from(x,y);
    }

    public int getX() {
        return coordinate.getX();
    }

    public int getY() {
        return coordinate.getY();
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

    /*
        Retorna true o false si hay una entidad en su interior o no
     */
    public boolean isFree() {
        return entity != null;
    }

    @Override
    public String toString() {
        return isFree() ? entity.toString() : " ";
    }

    /*
        Retorna true o false si el tile puede guardar en su interior una entidad
     */
    public boolean canOccupy() {
        return true;
    }

    public static Tile get(char c, int x, int y) {
        switch (c) {
            case '.':
                return Goal.get(x, y);
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
