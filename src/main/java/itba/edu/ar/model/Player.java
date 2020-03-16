package itba.edu.ar.model;

public class Player extends Entity {

    private Player(Tile tile) {
        super(tile);
    }

    public static Player get(Tile tile) {
        return new Player(tile);
    }

    @Override
    public boolean canMove(Tile[][] tileMap, Direction d) {
        /* EL casillero al cual me quiero mover */
        Tile nextTile = tileMap[this.getX() + d.getX()][this.getY() + d.getY()];
         /* "Si el tile al cual me quiero mover no es una Wall" */
        if (nextTile.canOccupy()) {
            /* "Si el tile al cual me quiero mover contiene una entidad en su interior, vemos si podemos mover a esta otra tmb" */
            if (!nextTile.isFree()) {
                return nextTile.getEntity().canMove(tileMap, d);
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "@";
    }
}
