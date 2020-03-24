package itba.edu.ar.model;


import java.util.*;
import java.util.function.BiFunction;


public class Heuristics {

    private BiFunction<Board,State, Integer> evaluate;
    private Map<Set<Coordinate>,Integer> pointsMap;

    public Heuristics(Board board, int heuristicNumber){
        if(heuristicNumber == 0){
            evaluate = this::evaluateBoxGoal;
        }else if (heuristicNumber == 1){
            evaluate = this::evaluatePerson;
        }else{
            evaluate = this::evaluatePositionPoints;

            Map<Coordinate,Map<Coordinate,Integer>> matrix= new HashMap<>();
            pointsMap = new HashMap<>();

            for (Coordinate goal: board.getGoals()) {
                matrix.put(goal, new HashMap<>());
            }

            calculateDistances(board,matrix);
            calculateBestDistances(board,matrix);
        }
    }

    private void calculateBestDistances(Board board,Map<Coordinate, Map<Coordinate, Integer>> goalBoxPoints) {
        State initialState = board.getInitialState();
        Queue<Coordinate> queue = new LinkedList<>(board.getGoals());
        Set<Coordinate> passedPlaces = new HashSet<>();
        while(!queue.isEmpty()){
            queue.poll();

        }

    }



    /**
     * Para cada goal, hago el puntaje(distancia minima sin atravesar
     * paredes) de cada posicion y lo guardo en la matriz.
     * @param board tablero de entrada
     * @param matrix matriz vacia en que guardo los punta
     */
    private void calculateDistances(Board board,Map<Coordinate,Map<Coordinate,Integer>> matrix) {
        int rows = board.getRows();
        int columns = board.getCols();

        for (Coordinate goal:board.getGoals()) {
            Set<Coordinate> passedPlaces = new HashSet<>();
            Queue<Coordinate> queue = new LinkedList<>();
            matrix.get(goal).put(goal,0);
            passedPlaces.add(goal);
            queue.add(goal);
            while(!queue.isEmpty()){
                Coordinate position = queue.poll();
                for (Direction dir:Direction.values()) {
                    Coordinate boxPos = position.move(dir);
                    Coordinate personPos = position.move(2,dir);
                    if(!(boxPos.getX() < 0 || boxPos.getX() >= rows || personPos.getY() < 0 || personPos.getY() >= columns) &&
                    !board.getWalls().contains(boxPos) && !board.getWalls().contains(personPos) &&
                            !passedPlaces.contains(boxPos)){
                        passedPlaces.add(boxPos);
                        int points = matrix.get(goal).get(position);
                        matrix.get(goal).put(boxPos,points + 1);
                    }
                }
            }
        }
    }

    /**
     * No Admisible. Hago la distancia tipo Manhattan entre las cajas y el objetivo
     * @param board
     * @param state
     * @return valor de la heuristica
     */
    private int evaluateBoxGoal(Board board, State state) {
        int ret = 0;

        for (Coordinate box : state.getBoxes()) {

            int minDistanceSum = Integer.MAX_VALUE;

            for (Coordinate goal : board.getGoals()) {
                int sum = 0;
                sum += Math.abs(goal.getX() - box.getX());
                sum += Math.abs(goal.getY() - box.getY());
                minDistanceSum = Math.min(sum, minDistanceSum);
            }
            ret += minDistanceSum;

        }
        return ret;
    }

//    private Set<Coordinate> evaluateBoxGoal(Set<Set<Coordinate>> boxPermuts, Set<Coordinate> boxes,Queue<Coordinate> goals){
//        if(boxes.size() == goals.size()){
//            return boxes;
//        }
//        for (int i = 0; i < goals.size(); i++) {
//            Coordinate coord = goals.
//            boxes.add();
//        }
//    }

    /**
     * Admisible
     * Con el puntaje de cada posicion, doy el puntaje de la permutacion de cajas
     * @param board
     * @param state
     * @return valor de la heuristica
     */
    private int evaluatePositionPoints(Board board, State state) {
        return pointsMap.get(state.getBoxes());
    }

    /**
     * No admisible
     * @param board
     * @param state
     * @return valor de la heuristica
     */
    private int evaluatePerson(Board board, State state) {
        int personBoxesDistance = 0;
        for (Coordinate box : state.getBoxes()) {
            personBoxesDistance +=
                    Math.abs(state.getPlayer().getX() - box.getX())
                            + Math.abs(state.getPlayer().getY() - box.getY());
        }
        return evaluateBoxGoal(board, state) + personBoxesDistance;
    }

    public BiFunction<Board, State, Integer> getEvaluate() {
        return evaluate;
    }

    private boolean compareBoxStates(State state1, State state2){
        return state1.getBoxes().equals(state2.getBoxes());
    }
}
