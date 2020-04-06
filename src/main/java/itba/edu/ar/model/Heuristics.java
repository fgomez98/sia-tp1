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
    }, PYTHAGORAS(){
        @Override
        public BiFunction<Board, State, Integer> getEvaluate() {
            return Heuristics::evaluatePythagorasPermutation;
        }
    };

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

    private static int evaluatePythagorasPermutation(Board board,State state){
        int ret = 0;

        for (Coordinate box : state.getBoxes()) {

            int minDistanceSum = Integer.MAX_VALUE;

            for (Coordinate goal : board.getGoals()) {
                minDistanceSum = Math.min(calculatePythagoras(goal, box), minDistanceSum);
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
                    sum += calculateManhattan(boxesList.get(j),state.getPlayer());
                    sum += boxPoints.get(boxesList.get(j)).get(goalsList.get(integers.get(j)));
                } catch (NullPointerException e) {
                    sum += Integer.MAX_VALUE / state.getBoxes().size();
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
//                sum += calculateManhattan(boxes.get(j),state.getPlayer());
                sum += calculateManhattan(boxes.get(j), goals.get(integers.get(j)));
            }
            ret = Math.min(ret, sum);
        }
//        ret *= 100;
//        int aux = Integer.MAX_VALUE;
//        for (Coordinate box:state.getBoxes()) {
//            aux = Math.min(aux,calculateManhattan(box,state.getPlayer()));
//        }
//
//        return ret+aux;
        return ret;
    }

    private static Integer calculateManhattan(Coordinate boxes, Coordinate goals) {
        int sum = 0;
        sum += Math.abs(goals.getX() - boxes.getX());
        sum += Math.abs(goals.getY() - boxes.getY());
        return sum;
    }
    private static int calculatePythagoras(Coordinate goals, Coordinate boxes) {
        return (int) Math.sqrt(Math.pow(goals.getX() - boxes.getX(),2) + Math.pow(goals.getY() - boxes.getY(),2));
    }

    public abstract BiFunction<Board, State, Integer> getEvaluate();
}
