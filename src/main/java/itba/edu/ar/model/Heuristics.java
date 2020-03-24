package itba.edu.ar.model;


import java.util.*;
import java.util.function.BiFunction;


public class Heuristics {

    private BiFunction<Board, State, Integer> evaluate;
    private Map<Set<Coordinate>, Integer> pointsMap;

    public Heuristics(Board board, int heuristicNumber) {
        if (heuristicNumber == 0) {
            evaluate = this::evaluateManhattan;
        } else if (heuristicNumber == 1) {
            evaluate = this::evaluateManhattanOpt;
        } else {
            evaluate = this::evaluatePointPosition;

            Map<Coordinate, Map<Coordinate, Integer>> matrix = new HashMap<>();
            pointsMap = new HashMap<>();

            for (Coordinate goal : board.getGoals()) {
                matrix.put(goal, new HashMap<>());
            }

            calculateDistances(board, matrix);
            calculateBestDistances(board, matrix);
        }
    }
    //calculo para cada una, la menor distancia de los goals
//        Queue<Coordinate> goalQueue = new LinkedList<>(board.getGoals());
//
//        for (Set<Coordinate> position: allPositions) {
//            List<Pair<Coordinate,Coordinate>> permutationsBoxGoal = new LinkedList<>();
//            for (Coordinate box:position) {
//                Coordinate goal = goalQueue.poll();
//                permutationsBoxGoal.add(new Pair<>(box,goal));
//                goalQueue.add(goal);
//            }
//            for (Pair<Coordinate,Coordinate> pair: permutationsBoxGoal) {
//                Set<Coordinate> aux = new HashSet<>();
//                Coordinate goal = pair.getValue();
//                Coordinate box = pair.getKey();
//                aux.add(box);
//                aux.add(goal);
//                int sum = 0;
//                sum += Math.abs(goal.getX() - box.getX());
//                sum += Math.abs(goal.getY() - box.getY());
//            }
//
//        }


    private void calculateBestDistances(Board board, Map<Coordinate, Map<Coordinate, Integer>> goalBoxPoints) {
        State initialState = board.getInitialState();
        Queue<Coordinate> queue = new LinkedList<>(board.getGoals());
        Set<Coordinate> passedPlaces = new HashSet<>();
        while (!queue.isEmpty()) {
            queue.poll();

        }

    }


    /**
     * Para cada goal, hago el puntaje(distancia minima sin atravesar
     * paredes) de cada posicion y lo guardo en la matriz.
     *
     * @param board  tablero de entrada
     * @param matrix matriz vacia en que guardo los punta
     */
    private void calculateDistances(Board board, Map<Coordinate, Map<Coordinate, Integer>> matrix) {
        int rows = board.getRows();
        int columns = board.getCols();

        for (Coordinate goal : board.getGoals()) {
            Set<Coordinate> passedPlaces = new HashSet<>();
            Queue<Coordinate> queue = new LinkedList<>();
            matrix.get(goal).put(goal, 0);
            passedPlaces.add(goal);
            queue.add(goal);
            while (!queue.isEmpty()) {
                Coordinate position = queue.poll();
                for (Direction dir : Direction.values()) {
                    Coordinate boxPos = position.move(dir);
                    Coordinate personPos = position.move(2, dir);
                    if (!(boxPos.getX() < 0 || boxPos.getX() >= rows || personPos.getY() < 0 || personPos.getY() >= columns) &&
                            !board.getWalls().contains(boxPos) && !board.getWalls().contains(personPos) &&
                            !passedPlaces.contains(boxPos)) {
                        passedPlaces.add(boxPos);
                        int points = matrix.get(goal).get(position);
                        matrix.get(goal).put(boxPos, points + 1);
                    }
                }
            }
        }
    }

    /**
     * No Admisible. Hago la distancia tipo Manhattan entre las cajas y el objetivo mas cercano
     *
     * @param board
     * @param state
     * @return valor de la heuristica
     */
    private int evaluateManhattan(Board board, State state) {
        int ret = 0;

        for (Coordinate box : state.getBoxes()) {

            int minDistanceSum = Integer.MAX_VALUE;

            for (Coordinate goal : board.getGoals()) {

                minDistanceSum = Math.min(calculateManhattan(goal, box), minDistanceSum);
            }
            ret += minDistanceSum;

        }
        return ret;
    }

    /**
     * Admisible
     * Con el puntaje de cada posicion, doy el puntaje de la permutacion de cajas
     *
     * @param board
     * @param state
     * @return valor de la heuristica
     */
    private int evaluatePointPosition(Board board, State state) {
        return pointsMap.get(state.getBoxes());
    }

    /**
     * Admisible Hago la distancia tipo Manhattan entre las cajas y el objetivo, usando la permutacion de menor valor
     *
     * @param board
     * @param state
     * @return valor de la heuristica
     */
    private int evaluateManhattanOpt(Board board, State state) {

        List<Coordinate> boxes = new ArrayList<>(state.getBoxes());
        List<Coordinate> goals = new ArrayList<>(board.getGoals());

        List<List<Integer>> combination = new LinkedList<>();
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < boxes.size(); i++) {
            numbers.add(i);
        }
        int ret = Integer.MAX_VALUE;
        permutationsOfIntegers(combination, new LinkedList<>(), numbers);
        for (List<Integer> integers : combination) {
            int sum = 0;
            for (int j = 0; j < boxes.size(); j++) {
                sum += calculateManhattan(boxes.get(j), goals.get(integers.get(j)));
            }
            ret = Math.min(ret, sum);
        }
        return ret;
    }

    private void permutationsOfIntegers(List<List<Integer>> combination, List<Integer> inter, List<Integer> goals) {
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

    private Integer calculateManhattan(Coordinate boxes, Coordinate goals) {
        int sum = 0;
        sum += Math.abs(goals.getX() - boxes.getX());
        sum += Math.abs(goals.getY() - boxes.getY());
        return sum;
    }

    public BiFunction<Board, State, Integer> getEvaluate() {
        return evaluate;
    }
}
