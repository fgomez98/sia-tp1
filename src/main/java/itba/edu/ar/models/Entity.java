package itba.edu.ar.models;

public abstract class Entity {

    private Tile tile; // casillero en el que se encuentra

    Entity(Tile tile) {
        this.tile = tile;
    }

    public int getX() {
        return tile.getX();
    }

    public int getY() {
        return tile.getY();
    }

    public void put() {
        this.tile.setEntity(this);
    }

    public void remove() {
        this.tile.setEntity(null);
    }

    public boolean move(Tile nextTile) {
        return true;
    }
}
