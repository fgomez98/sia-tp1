package itba.edu.ar.model;

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

    public void put(Tile tile) {
        this.tile = tile;
        this.put();
    }

    public Tile getTile() {
        return tile;
    }

    public void remove() {
        this.tile.setEntity(null);
    }

    public abstract boolean canMove(Tile[][] tileMap, Direction d);

}
