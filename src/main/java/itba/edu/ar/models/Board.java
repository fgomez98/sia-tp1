package itba.edu.ar.models;

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
            if (t.isOccupied()) {
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

    public static void main(String[] args) {
        Board b = Board.from("./src/main/resources/Levels/Level 1");
        System.out.println(b);
    }

}
