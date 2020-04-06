package itba.edu.ar.model;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static itba.edu.ar.model.Direction.*;
import static itba.edu.ar.model.Entity.*;

public class Board {

    private int rows;
    private int cols;
    private final Set<Coordinate> goals;
    private final Set<Coordinate> walls;
    private final Set<Coordinate> deadBoxes;
    private Set<Coordinate> boxesInitial;
    private Coordinate playerInitial;
    private Map<Coordinate, Map<Coordinate, Integer>> boxGoalPoints = new HashMap<>();
    private List<List<Integer>> combination;

    /*
        Se asume que los tableros son correctos y que siempre hay un jugador en los mismos
    */

    private Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.goals = new HashSet<>();
        this.walls = new HashSet<>();
        this.deadBoxes = new HashSet<>();
    }

    private Board() {
        this.boxesInitial = new HashSet<>();
        this.goals = new HashSet<>();
        this.walls = new HashSet<>();
        this.deadBoxes = new HashSet<>();
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


        if(board.getGoals().size() < 5){
            board.combination = new LinkedList<>();
            List<Integer> numbers = new ArrayList<>();
            for (int i = 0; i < board.getGoals().size(); i++) {
                numbers.add(i);
            }
            permutationsOfIntegers(board.combination, new LinkedList<>(), numbers);
        }

        board.rows = x;
        board.cols = yMax;
        board.analyzeBoard();
        board.printPoints();
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
            case GOAL_BOX:
                boxesInitial.add(Coordinate.from(x, y));
                goals.add(Coordinate.from(x, y));
                break;
            default:
                break;
        }
    }

    /**
     * Para cada goal, hago el puntaje(distancia minima sin atravesar
     * paredes) de cada posicion y lo guardo en una matriz que para cada caja, muestra el puntaje a cada goal.
     * Agrego ademas a la lista de deadboxes a todas las posiciones que no llego
     */
    public void analyzeBoard() {

        /* can not reach goal */
        Set<Coordinate> visited = new HashSet<>();
        for (Coordinate coord : goals) {
            //boxGoalPoints.put(coord, new HashMap<>());
            pullFromGoal(coord, visited);
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Coordinate coord = Coordinate.from(i, j);
                if (isCorner(coord) || !visited.contains(coord)) {
                    /* corners */
                    /* every square not being marked as visited is a simple deadlock square */
                    deadBoxes.add(coord);
                }
            }
        }
//        for (Coordinate position : boxGoalPoints.keySet()){
//            for (Coordinate goal : goals){
//                boxGoalPoints.get(position).putIfAbsent(goal, Integer.MAX_VALUE/goals.size());
//            }
//        }
    }

    private boolean isCorner(Coordinate coord) {
        if (goals.contains(coord)) {
            return false;
        } else if (walls.contains(coord.move(UP)) && walls.contains(coord.move(RIGHT))) { // Top Right Corner
            return true;
        } else if (walls.contains(coord.move(UP)) && walls.contains(coord.move(LEFT))) { // Top Left Corner
            return true;
        } else if (walls.contains(coord.move(DOWN)) && walls.contains(coord.move(RIGHT))) { // Bottom Right Corner
            return true;
        } else if (walls.contains(coord.move(DOWN)) && walls.contains(coord.move(LEFT))) { // Bottom Left Corner
            return true;
        }
        return false;
    }

    private void pullFromGoal(Coordinate goal, Set<Coordinate> visited) {
        Set<Coordinate> passedPlaces = new HashSet<>();
        Queue<Coordinate> queue = new LinkedList<>();
        boxGoalPoints.computeIfAbsent(goal, k -> new HashMap<>());
        boxGoalPoints.get(goal).put(goal, 0);
        passedPlaces.add(goal);
        visited.add(goal);
        queue.add(goal);
        while (!queue.isEmpty()) {
            Coordinate position = queue.poll();
            for (Direction d : directions) {
                Coordinate boxPos = position.move(d);
                Coordinate personPos = position.move(2, d);
                if (inBounds(boxPos) && inBounds(personPos)
                        && !walls.contains(boxPos) && !walls.contains(personPos)) {
                    if(!passedPlaces.contains(boxPos) || (boxGoalPoints.get(boxPos).get(goal) > boxGoalPoints.get(position).get(goal)+1)){
                        if(!passedPlaces.contains(boxPos)){
                            passedPlaces.add(boxPos);
                            visited.add(boxPos);
                        }
                        int points = boxGoalPoints.get(position).get(goal);
                        boxGoalPoints.computeIfAbsent(boxPos, k -> new HashMap<>());
                        boxGoalPoints.get(boxPos).put(goal,points+1);
                        queue.offer(boxPos);
                        passedPlaces.add(boxPos);
                    }
                }
            }
        }
    }

    private boolean inBounds(Coordinate coord) {
        return coord.getX() < rows && coord.getX() >= 0 && coord.getY() < cols && coord.getY() >= 0;
    }

    public boolean isComplete(State state) {
        for (Coordinate boxCoords : state.getBoxes()) {
            if (!goals.contains(boxCoords)) {
                return false;
            }
        }
        return true;
    }

    /*
        Frozen boxes don't create a Freeze deadlock when being located on a goal. Nevertheless they may influence the reachable area of other boxes.
     */
    private boolean isBoxBlocked(Coordinate box, State state) {
        Set<Coordinate> aux = new HashSet<>();
        return isBoxBlockedRec(box, state, aux);
    }

    private boolean isBoxBlockedRec(Coordinate box, State state, Set<Coordinate> visitedBoxes) {
        visitedBoxes.add(box);
        // x-axis
        Coordinate left = box.move(LEFT);
        Coordinate right = box.move(RIGHT);
        // y-axis
        Coordinate up = box.move(UP);
        Coordinate down = box.move(DOWN);
        // si esta bloqueada en ambos ejes es un deadlock
        return isBoxAxisBlocked(left, right, state, visitedBoxes) & isBoxAxisBlocked(up, down, state, visitedBoxes);
    }

    private boolean isBoxAxisBlocked(Coordinate sideA, Coordinate sideB, State state, Set<Coordinate> visitedBoxes) {
        if (walls.contains(sideA) || walls.contains(sideB)) {
            return true;
        } else if (deadBoxes.contains(sideA) & deadBoxes.contains(sideB)) {
            return true;
        } else if (visitedBoxes.contains(sideA) || visitedBoxes.contains(sideB)) {
            return true;
        } else if (state.getBoxes().contains(sideA)) {
            return isBoxBlockedRec(sideA, state, visitedBoxes);
        } else if (state.getBoxes().contains(sideB)) {
            return isBoxBlockedRec(sideB, state, visitedBoxes);
        }
        return false;
    }

    private boolean isDeadlock(State state) {
        for (Coordinate coord : state.getBoxes()) {
            if (deadBoxes.contains(coord)) {
                return true;
            } else if (!goals.contains(coord) && isBoxBlocked(coord, state)) {
                return true;
            }
        }
        return false;
    }

    public List<Direction> getPosibleMovements(State state) {
        List<Direction> movements = new LinkedList<>();
        if (isDeadlock(state)) {
            return movements;
        }
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
        return print(state, false);
    }

    private String print(State state, boolean withdeadBoxes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Coordinate coord = Coordinate.from(i, j);
                if (walls.contains(coord)) {
                    sb.append(WALL.toString());
                } else if (withdeadBoxes && deadBoxes.contains(coord)) {
                    sb.append("X");
                } else if (state.getBoxes().contains(coord)) {
                    if (goals.contains(coord)) {
                        sb.append(GOAL_BOX.toString());
                        continue;
                    }
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

    private String printPoints() {
        StringBuilder sb = new StringBuilder();
        for (Coordinate goal:goals){
            sb.append("goal: ").append(goal.toString()).append('\n');
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    boolean flag = true;
                    Coordinate coord = Coordinate.from(i, j);
                    if (walls.contains(coord)) {
                        sb.append(WALL.toString());
                        flag = false;
                    }else if(boxGoalPoints.get(coord) != null){
                        if(boxGoalPoints.get(coord).get(goal) != null){
                            String a = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
                            sb.append(a.toCharArray()[boxGoalPoints.get(coord).get(goal)]);
                        }else{
                            sb.append(' ');
                        }
                        flag = false;
                    }
                    if(flag && !walls.contains(coord) &&(boxGoalPoints.get(coord) == null || boxGoalPoints.get(coord).get(goal) != null)){
                        sb.append(' ');
                    }
                }
                sb.append('\n');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    public String printDeadBoxes() {
        return print(getInitialState(), true);
    }

    public Set<Coordinate> getGoals() {
        return goals;
    }

    public Set<Coordinate> getWalls() {
        return walls;
    }

    public List<List<Integer>> getCombination() {
        return combination;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Map<Coordinate, Map<Coordinate, Integer>> getBoxGoalPoints() {
        return boxGoalPoints;
    }

    @Override
    public String toString() {
        return print(getInitialState());
    }

    public static void main(String[] args) {
        Board board = Board.from("./src/main/resources/Levels/Level 11");
        State state = board.getInitialState();

        System.out.println("Type Up, Down, Right, Left to move sokoban");

        while (!board.isComplete(state)) {
            System.out.println(board.print(state));

            StringBuilder sb = new StringBuilder("Posible movements: ");
            List<Direction> posibleMovments = board.getPosibleMovements(state);

            if (posibleMovments.isEmpty()) {
                System.out.println("Your deadlock...");
                break;
            }

            posibleMovments.forEach(direction -> sb.append(direction.name()).append(' '));
            System.out.println(sb.toString());

            System.out.println("Your move...");

            Scanner input = new Scanner(System.in);
            String line = input.nextLine();

            Optional<Direction> movement = Direction.from(line);

            if (movement.isPresent()) {
                if (posibleMovments.contains(movement.get())) {
                    state = board.move(state, movement.get());
                    continue;
                }
            }
            System.out.println("invalid movement");
        }

    }

    private static void permutationsOfIntegers(List<List<Integer>> combination, List<Integer> inter, List<Integer> goals) {
        if (goals.isEmpty()) {
            combination.add(new ArrayList<>(inter));
            return;
        }
        List<Integer> goalAux = new LinkedList<>(goals);
        for (Integer goal : goals) {
            goalAux.remove(goal);
            inter.add(goal);
            permutationsOfIntegers(combination, inter, goalAux);
            goalAux.add(goal);
            inter.remove(goal);
        }
    }
}
