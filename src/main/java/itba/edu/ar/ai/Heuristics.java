package itba.edu.ar.ai;


import itba.edu.ar.model.Board;
import itba.edu.ar.model.Coordinate;
import itba.edu.ar.model.State;

import java.util.Set;


public class Heuristics {

    static int evaluate(Board board, State state) {
        int minDistanceSum = Integer.MAX_VALUE;
        for (Coordinate box : state.getBoxes()) {
            int sum = 0;
            for (Coordinate goal : board.getGoals()) {
                sum += Math.abs(goal.getX() - box.getX());
                sum += Math.abs(goal.getY() - box.getY());
            }
            minDistanceSum = Math.min(sum, minDistanceSum);
        }
        return minDistanceSum;
    }

    static int evaluatePersonGoal(Board board, State state) {
        int minDistance = Integer.MAX_VALUE;

        for (Coordinate goal : board.getGoals()) {



        }


        return minDistance;
    }

    static int evaluatePerson(Board board, State state) {
        int personBoxMinDistance = Integer.MAX_VALUE;
        for (Coordinate box : state.getBoxes()) {
            personBoxMinDistance = Math.min(personBoxMinDistance,
                    Math.abs(state.getPlayer().getX() - box.getX())
                            + Math.abs(state.getPlayer().getY() - box.getY()));
        }
        return evaluate(board, state) + personBoxMinDistance;
    }

}
