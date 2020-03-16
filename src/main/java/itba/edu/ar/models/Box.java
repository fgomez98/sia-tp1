package itba.edu.ar.models;

public class Box extends Entity {

    private Box(Tile tile) {
        super(tile);
    }

    public static Box get(Tile tile) {
        return new Box(tile);
    }

    @Override
    public boolean canMove(Tile[][] tileMap, Directions d) {
         /* EL casillero al cual me quiero mover */
        Tile nextTile = tileMap[this.getX() + d.getX()][this.getY() + d.getY()];
        if (nextTile.canOccupy()) {
            /* "Si el tile al cual me quiero mover no es una Wall" */
            if (!nextTile.isFree()) {
                /* "Si el tile al cual me quiero mover contiene una entidad en su interior, la Box no podra moverse ya que no 'empuja a otras entidades' " */
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "$";
    }

}
