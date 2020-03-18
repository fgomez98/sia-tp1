package itba.edu.ar.model;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static itba.edu.ar.model.Entity.*;

public class Board {

    private int rows;
    private int cols;
    private final Set<Coordinate> goals;
    private final Set<Coordinate> walls;
    private Set<Coordinate> boxesInitial;
    private Coordinate playerInitial;

    /*
        Se asume que los tableros son correctos y que siempre hay un jugador en los mismos
    */

    private Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.goals = new HashSet<>();
        this.walls = new HashSet<>();
    }

    private Board() {
        this.boxesInitial = new HashSet<>();
        this.goals = new HashSet<>();
        this.walls = new HashSet<>();
    }

    public static Board from(String boardFilename) {
        Board board = new Board();
        int x = 0, y = 0;
        int yMax = 0;

        try (Scanner scanner = new Scanner(new File(boardFilename))) {
            while (scanner.hasNextLine()) {
                y = 0;
                char[] chars = scanner.nextLine().toCharArray();
                for (char c : chars) {
                    board.store(Entity.from(c), x, y++);
                }
                x++;
                yMax = Math.max(yMax, chars.length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        board.rows = x;
        board.cols = yMax;
        return board;
    }

    private void store(Entity e, int x, int y) {
        switch (e) {
            case BOX:
                boxesInitial.add(Coordinate.from(x, y));
                break;
            case GOAL:
                goals.add(Coordinate.from(x, y));
                break;
            case PLAYER:
                playerInitial = Coordinate.from(x, y);
                break;
            case WALL:
                walls.add(Coordinate.from(x, y));
                break;
            default:
                break;
        }
    }

    public boolean isComplete(State state) {
        for (Coordinate boxCoords : state.getBoxes()) {
            if (!goals.contains(boxCoords)) {
                return false;
            }
        }
        return true;
    }

    public List<Direction> getPosibleMovements(State state) {
        List<Direction> movements = new LinkedList<>();
        for (Direction d : Direction.directions) {
            if (playerCanMove(state, d)) {
                movements.add(d);
            }
        }
        return movements;
    }

    private boolean playerCanMove(State prevState, Direction d) {
        Coordinate nextPlayerCoords = prevState.getPlayer().move(d);
        if (walls.contains(nextPlayerCoords)) {
            return false;
        } else if (prevState.getBoxes().contains(nextPlayerCoords)) {
            return boxCanMove(prevState, nextPlayerCoords, d);
        }
        // es un goal o tile
        return true;
    }

    private boolean boxCanMove(State state, Coordinate coords, Direction d) {
        Coordinate nextBoxCoords = coords.move(d);
        return !walls.contains(nextBoxCoords) && !state.getBoxes().contains(nextBoxCoords); /* != PLAYER (caso que no se da nunca) */
    }

    /*
        Se asume que no se va a llamar sin antes haber consultado a getPosibleMovements
        Retorna el proximo estado una vez realizado el movimiento
     */
    public State move(State prevState, Direction d) {
        Coordinate nextStatePlayer = prevState.getPlayer().move(d);
        State nextState = State.from(prevState.getBoxes(), nextStatePlayer);

        /*
            La lista de cajas en nextState es igual a la de prevState.
            Si habia una caja en la posicion nueva del jugador tenemos que moverla
            De esta forma el movimiento solo se efectua en nextState no en prevState
        */

        if (nextState.getBoxes().contains(nextStatePlayer)) {
            Coordinate oldBoxCoords = nextStatePlayer;
            Coordinate newBoxCoords = oldBoxCoords.move(d);
            nextState.getBoxes().remove(oldBoxCoords);
            nextState.getBoxes().add(newBoxCoords);
        }
        return nextState;
    }

    public State getInitialState() {
        return State.from(boxesInitial, playerInitial);
    }

    public String print(State state) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Coordinate coord = Coordinate.from(i, j);
                if (walls.contains(coord)) {
                    sb.append(WALL.toString());
                } else if (state.getBoxes().contains(coord)) {
                    sb.append(BOX.toString());
                } else if (state.getPlayer().equals(coord)) {
                    sb.append(PLAYER.toString());
                } else if (goals.contains(coord)) {
                    sb.append(GOAL.toString());
                } else {
                    sb.append(TILE.toString());
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return print(getInitialState());
    }

    public static void main(String[] args) {
        Board board = Board.from("./src/main/resources/Levels/Level 1");
        State state = board.getInitialState();

        System.out.println("Type Up, Down, Right, Left to move sokoban");

        while (!board.isComplete(state)) {
            System.out.println(board.print(state));

            System.out.println("We asume that you will NOT enter an invalid movement");
            StringBuilder sb = new StringBuilder("Posible movements: ");
            board.getPosibleMovements(state).forEach(direction -> sb.append(direction.name()).append(' '));
            System.out.println(sb.toString());

            System.out.println("Your move...");

            Scanner input = new Scanner(System.in);
            String line = input.nextLine();

            Direction movement = Direction.valueOf(line.trim().toUpperCase());

            state = board.move(state, movement);
        }

    }
}
