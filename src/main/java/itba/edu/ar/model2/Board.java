package itba.edu.ar.model2;

import itba.edu.ar.model.Coordinate;
import itba.edu.ar.model.Direction;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import static itba.edu.ar.model2.Entity.*;

public class Board {

    private int rows;
    private int cols;
    private Entity[][] tiles;
    private final List<Coordinate> goals;
    private List<Coordinate> boxes;
    private Coordinate player;

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Entity[][] getTiles() {
        return tiles;
    }

    public List<Coordinate> getGoals() {
        return goals;
    }

    public List<Coordinate> getBoxes() {
        return boxes;
    }

    public Coordinate getPlayer() {
        return player;
    }

    /*
        Se asume que los tableros son correctos y que siempre hay un jugador en los mismos
    */

    private Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.tiles = new Entity[rows][cols];
        this.boxes = new LinkedList<>();
        this.goals = new LinkedList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (tiles[i][j] == null) {
                    /*
                        Llenamos los espacios vacios de la matriz con celdas regulares para evitar (null)
                     */
                    tiles[i][j] = Entity.TILE;
                }
            }
        }
    }

    public static Board from(String boardFilename) {
        int rows = 0, cols = 0;

        try (Scanner scanner = new Scanner(new File(boardFilename))) {
            while (scanner.hasNextLine()) {
                rows++;
                cols = Math.max(cols, scanner.nextLine().length());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Board board = new Board(rows, cols);
        int x = 0, y = 0;

        try (Scanner scanner = new Scanner(new File(boardFilename))) {
            while (scanner.hasNextLine()) {
                y = 0;
                char[] chars = scanner.nextLine().toCharArray();
                for (char c : chars) {
                    board.tiles[x][y] = Entity.from(c);
                    board.store(board.tiles[x][y], x, y++);
                }
                x++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return board;
    }

    private void store(Entity e, int x, int y) {
        switch (e) {
            case BOX:
                boxes.add(Coordinate.from(x, y));
                break;
            case GOAL:
                goals.add(Coordinate.from(x, y));
                break;
            case PLAYER:
                player = Coordinate.from(x, y);
                break;
            default:
                break;
        }
    }

    public boolean isComplete() {
        for (Coordinate goal : goals) {
            if (tiles[goal.getX()][goal.getY()] != BOX) {
                return false;
            }
        }
        return true;
    }

    public List<Direction> getPosibleMovements() {
        List<Direction> movements = new LinkedList<>();
        for (Direction d : Direction.directions) {
            if (playerCanMove(player, d)) {
                movements.add(d);
            }
        }
        return movements;
    }

    private boolean playerCanMove(Coordinate cords, Direction d) {
        Entity nextTile = tiles[player.getX() + d.getX()][player.getY() + d.getY()];
        if (nextTile != WALL) {
            if (nextTile == BOX) {
                return boxCanMove(cords.move(d), d);
            } else { /* Es un TILE o GOAL, podemos movernos aqui */
                return true;
            }
        } else {
            return false;
        }
    }

    private boolean boxCanMove(Coordinate coords, Direction d) {
        Entity nextTile = tiles[coords.getX() + d.getX()][coords.getY() + d.getY()];
        return nextTile != WALL && nextTile != BOX; /* != PLAYER (caso que no se da nunca) */
    }

    /*
        Se asume que no se va a llamar sin antes haber consultado a getPosibleMovements
        Lo que se esta moviendo es el jugador
     */
    public void move(Direction d) {
        Coordinate nextTileCoords = player.move(d);
        Entity nextTile = tiles[nextTileCoords.getX()][nextTileCoords.getY()];
        if (nextTile == BOX) {
            /* Si hay una caja tenemos que mover esta primero */
            /* Update a las coordenadas de las cajas en el tablero */
            Coordinate oldBoxCoords = nextTileCoords;
            Coordinate newBoxCoords = Coordinate.from(player.getX() + 2 * d.getX(), player.getY() + 2 * d.getY());
            boxes.remove(oldBoxCoords);
            boxes.add(newBoxCoords);
            /* movemos la caja */
            tiles[newBoxCoords.getX()][newBoxCoords.getY()] = BOX; /* Piso directo, si se llamo antes a getPosibleMovements este es un movimiento valido*/
            tiles[oldBoxCoords.getX()][oldBoxCoords.getY()] = null; /* Lo pongo en null ya que dsp se va a pisar con el jugador */
        }
        /* No hay nadie en el Tile, procedemos a movernos ahi */
        Coordinate oldPlayerCoords = player;
        player = nextTileCoords;
        tiles[nextTileCoords.getX()][nextTileCoords.getY()] = PLAYER;
        /*
            El jugador estaba parado en un TILE normal o en un GOAL, nunca en un BOX o WALL.
            Podriamos dejar un TILE, total no me importa mucho, ya que uno la lista de coordenadas de GOAL para ver si gano o no u otros.
            Entonces solo me interesaria updetear las posiciones de los GOALS en sus lugares corresponientes cuando imprimo
         */
        tiles[oldPlayerCoords.getX()][oldPlayerCoords.getY()] = goals.contains(oldPlayerCoords) ? GOAL : TILE;
    }

    public void changePlayingState(Coordinate player, List<Coordinate> boxes) {
        this.boxes = boxes;
        this.player = player;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sb.append(tiles[i][j].toString());
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Board board = Board.from("./src/main/resources/Levels/Level 1");

        System.out.println("Type Up, Down, Right, Left to move sokoban");

        while (!board.isComplete()) {
            System.out.println(board);

            System.out.println("We asume that you will NOT enter an invalid movement");
            StringBuilder sb = new StringBuilder("Posible movements: ");
            board.getPosibleMovements().forEach(direction -> sb.append(direction.name()).append(' '));
            System.out.println(sb.toString());

            System.out.println("Your move...");

            Scanner input = new Scanner(System.in);
            String line = input.nextLine();

            Direction movement = Direction.valueOf(line.trim().toUpperCase());

            board.move(movement);
        }

    }
}
