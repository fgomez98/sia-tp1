package itba.edu.ar.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Board {

    private Tile[][] tiles;
    private List<Wall> walls;
    private List<Box> boxes;
    private List<Goal> goals;
    private List<Tile> emptyTiles;
    private Player player;
    private final int rows;
    private final int cols;

    /*
        Se asume que los tableros son correctos y que siempre hay un jugador en los mismos
    */

    private Board(final int rows, final int cols, List<Tile> tilesList) {
        this.rows = rows;
        this.cols = cols;
        this.tiles = new Tile[rows][cols];
        goals = new LinkedList<>();
        boxes = new LinkedList<>();
        walls = new LinkedList<>();
        emptyTiles = new LinkedList<>();
        for (Tile t : tilesList) {
            tiles[t.getX()][t.getY()] = t;
            if (t instanceof Goal) {
                goals.add((Goal) t);
            } else if (t instanceof Wall) {
                walls.add((Wall) t);
            }
            if (!t.isFree()) {
                if (t.getEntity() instanceof Player) {
                    player = (Player) t.getEntity();
                } else if (t.getEntity() instanceof Box) {
                    boxes.add((Box) t.getEntity());
                }
            }
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (tiles[i][j] == null) {
                    /*
                        Llenamos los espacios vacios de la matriz con celdas regulares para evitar (null)
                     */
                    tiles[i][j] = Tile.get(i, j);
                    emptyTiles.add(tiles[i][j]);
                }
            }
        }
    }

    public static Board from(String boardFilename) {
        List<Tile> tiles = new ArrayList<>();
        int X = 0, Y = 0;
        try (Scanner scanner = new Scanner(new File(boardFilename))) {
            while (scanner.hasNext()) {
                int y = 0;
                char[] chars = scanner.nextLine().toCharArray();
                for (char c : chars) {
                    tiles.add(Tile.get(c, X, y++));
                }
                X++;
                Y = Math.max(y, Y);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Board(X, Y, tiles);
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

    public Player getPlayer() {
        return player;
    }

    public List<Wall> getWalls() {
        return walls;
    }

    public List<Box> getBoxes() {
        return boxes;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public List<Tile> getEmptyTiles() {
        return emptyTiles;
    }

    public boolean isComplete() {
        for (Goal g : goals) {
            if (g.isFree() || !g.isFree() && !(g.getEntity() instanceof Box)) {
                return false;
            }
        }
        return true;
    }

    public List<Direction> getPosibleMovements() {
        List<Direction> movements = new LinkedList<>();
        for (Direction d : Direction.directions) {
            if (player.canMove(tiles, d)) {
                movements.add(d);
            }
        }
        return movements;
    }

    private void move(Entity entity, Direction direction) {
        /* EL casillero al cual me quiero mover */
        Tile nextTile = tiles[entity.getX() + direction.getX()][entity.getY() + direction.getY()];

        if (!entity.canMove(tiles, direction)) {
            return;
        }

        /* Primero movemos a la entidad  que se encuentra en el casillero si es que hay una */
        if (!nextTile.isFree()) {
            move(nextTile.getEntity(), direction);
        }

        /* No hay nadie en el Tile, procedemos a movernos ahi */
        entity.remove(); /* Me voy de mi tile */
        entity.put(nextTile); /* Me posiciono en el proximo tile */
    }

    public void changePlayingState(State state) {
        this.player.remove();
        this.boxes.forEach(Box::remove);
        state.getPlayer().put();
        state.getBoxes().forEach(Box::put);
    }

    public State getState() {
        return State.from(boxes, player);
    }
    /*
        Mueve el jugador
     */
    public void move(Direction direction) {
        move(player, direction);
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

