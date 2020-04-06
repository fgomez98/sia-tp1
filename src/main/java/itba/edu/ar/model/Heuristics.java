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
    },GREEDY_ASSIGNMENT(){
        @Override
        public BiFunction<Board, State, Integer> getEvaluate() {
            return Heuristics::evaluateGreedy;
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
            if(!board.getGoals().contains(box))
                ret += minDistanceSum + calculateManhattan(state.getPlayer(), box);
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
                    if(!board.getGoals().contains(boxesList.get(j)))
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
                sum += calculateManhattan(boxes.get(j),state.getPlayer());
                sum += calculateManhattan(boxes.get(j), goals.get(integers.get(j)));
            }
            ret = Math.min(ret, sum);
        }
        return ret;
    }

    private static Integer evaluateGreedy(Board board, State state){
        int ret = 0;
        Set<Coordinate> assignedBoxes = new HashSet<>();
        Set<Coordinate> assignedGoals = new HashSet<>();
        Set<Pair<Coordinate,Coordinate>> matches = new HashSet<>();
        PriorityQueue<Pair<Coordinate,Coordinate>> pq = new PriorityQueue<>(Comparator.comparingInt(o -> board.getBoxGoalPoints().get(o.getKey()).get(o.getValue())));
        for (Coordinate box: state.getBoxes()) {
            for (Coordinate goal: board.getGoals()) {
                if (board.getBoxGoalPoints().get(box) == null){
                    return Integer.MAX_VALUE;
                }
                if(board.getBoxGoalPoints().get(box).get(goal) != null){
                    pq.offer(new Pair<>(box, goal));
                }
            }
        }
        while (!pq.isEmpty()){
            Pair<Coordinate,Coordinate> boxGoal = pq.poll();
            if(!assignedBoxes.contains(boxGoal.getKey()) && !assignedGoals.contains(boxGoal.getValue())){
                assignedBoxes.add(boxGoal.getKey());
                assignedGoals.add(boxGoal.getValue());
                matches.add(boxGoal);
                if(matches.size() == state.getBoxes().size()){
                    break;
                }
            }
        }
        for (Coordinate box: state.getBoxes()) {
            if(!assignedBoxes.contains(box)){
                int aux = Integer.MAX_VALUE/state.getBoxes().size();
                for (Map.Entry<Coordinate,Integer> goal: board.getBoxGoalPoints().get(box).entrySet()) {
                    aux = Math.min(aux, goal.getValue());
                }
                ret += aux;
            }
        }
        for (Pair<Coordinate,Coordinate> match: matches) {
            ret += board.getBoxGoalPoints().get(match.getKey()).get(match.getValue());
            if(!board.getGoals().contains(match.getKey()))
                ret += calculateManhattan(state.getPlayer(), match.getKey());
        }
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
