package itba.edu.ar.model;

import java.util.*;
import java.util.function.BiFunction;

public enum Heuristics {

    MANHATTAN() {
        @Override
        public BiFunction<Board, State, Integer> getEvaluate() {
            return Heuristics::evaluateManhattan;
        }
    }, MANHATTAN_OPT() {
        @Override
        public BiFunction<Board, State, Integer> getEvaluate() {
            return Heuristics::evaluateManhattanOpt;
        }
    }, POINT_POSITION_OPT() {
        @Override
        public BiFunction<Board, State, Integer> getEvaluate() {
            return Heuristics::evaluatePointPosition;
        }
    },POINT_POSITION(){
        @Override
        public BiFunction<Board, State, Integer> getEvaluate() {
            return Heuristics::evaluatePointsNoPermutation;
        }
    };

    private BiFunction<Board, State, Integer> evaluate;
//    private Map<Set<Coordinate>, Integer> pointsMap; //box positions and point


//    public Heuristics(Board board, int heuristicNumber) {
//        if (heuristicNumber == 0) {
//            evaluate = this::evaluateManhattan;
//        } else if (heuristicNumber == 1) {
//            evaluate = this::evaluateManhattanOpt;
//        } else {
//            evaluate = this::evaluatePointPosition;

//            Map<Coordinate, Map<Coordinate, Integer>> matrix = new HashMap<>();
//            pointsMap = new HashMap<>();
//
//            for (Coordinate goal : board.getGoals()) {
//                matrix.put(goal, new HashMap<>());
//            }
//
//            calculateDistances(board, matrix);
    //calculateBestDistances(board, matrix);
//        }
//    }

    /**
     * Para cada goal, hago el puntaje(distancia minima sin atravesar
     * paredes) de cada posicion y lo guardo en la matriz.
     *
     * @param board  tablero de entrada
     * @param matrix matriz vacia en que guardo los punta
     */
   /* private void calculateDistances(Board board, Map<Coordinate, Map<Coordinate, Integer>> matrix) {
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
                        queue.add(boxPos);
                    }
                }
            }
        }
    }*/

    /**
     * No Admisible. Hago la distancia tipo Manhattan entre las cajas y el objetivo mas cercano
     *
     * @param board
     * @param state
     * @return valor de la heuristica
     */
    private static int evaluateManhattan(Board board, State state) {
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

    private static int evaluatePointsNoPermutation(Board board, State state) {
        int ret = 0;

        for (Coordinate box : state.getBoxes()) {

            int minDistanceSum = Integer.MAX_VALUE;

            for (Coordinate goal : board.getGoals()) {
                try {
                    minDistanceSum = Math.min(board.getBoxGoalPoints().get(box).get(goal), minDistanceSum);
                }catch (NullPointerException ignored){
                }
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
    private static int evaluatePointPosition(Board board, State state) {

        List<Coordinate> boxesList = new LinkedList<>(state.getBoxes());
        List<Coordinate> goalsList = new LinkedList<>(board.getGoals());
        Map<Coordinate, Map<Coordinate, Integer>> boxPoints = board.getBoxGoalPoints();

        int ret = Integer.MAX_VALUE;
        List<List<Integer>> combination = board.getCombination();

        for (List<Integer> integers : combination) {
            int sum = 0;
            for (int j = 0; j < boxesList.size(); j++) {
                try {
                    sum += boxPoints.get(boxesList.get(j)).get(goalsList.get(integers.get(j)));
                } catch (NullPointerException e) {
                    sum += Integer.MAX_VALUE / 2;
                }
            }
            ret = Math.min(ret, sum);
        }


        return ret;
    }

    /**
     * Admisible Hago la distancia tipo Manhattan entre las cajas y el objetivo, usando la permutacion de menor valor
     *
     * @param board
     * @param state
     * @return valor de la heuristica
     */
    private static int evaluateManhattanOpt(Board board, State state) {

        List<Coordinate> boxes = new ArrayList<>(state.getBoxes());
        List<Coordinate> goals = new ArrayList<>(board.getGoals());

        List<List<Integer>> combination = board.getCombination();

        int ret = Integer.MAX_VALUE;
        for (List<Integer> integers : combination) {
            int sum = 0;
            for (int j = 0; j < boxes.size(); j++) {
                sum += calculateManhattan(boxes.get(j), goals.get(integers.get(j)));
            }
            ret = Math.min(ret, sum);
        }
        return ret;
    }

    /*private static List<List<Integer>> permutationsOfIntegers(List<Coordinate> boxes) {
        List<List<Integer>> combination = new LinkedList<>();
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < boxes.size(); i++) {
            numbers.add(i);
        }
        permutationsOfIntegers(combination, new LinkedList<>(), numbers);
        return combination;
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
*/
    private static Integer calculateManhattan(Coordinate boxes, Coordinate goals) {
        int sum = 0;
        sum += Math.abs(goals.getX() - boxes.getX());
        sum += Math.abs(goals.getY() - boxes.getY());
        return sum;
    }

    public abstract BiFunction<Board, State, Integer> getEvaluate();
}
