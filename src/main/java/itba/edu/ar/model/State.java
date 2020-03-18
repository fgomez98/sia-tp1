package itba.edu.ar.model;

import java.util.LinkedList;
import java.util.List;

public class State {

    private List<Box> boxes;
    private Player player;

    private State(List<Box> boxes, Player player) {
        this.boxes = boxes;
        this.player = player;
    }

    static State from(List<Box> boxes, Player player) {
        List<Box> boxesAux = new LinkedList<>();
        boxes.forEach(box -> boxesAux.add(Box.get(box.getTile())));
        return new State(boxesAux, Player.get(player.getTile()));
    }

//    public static State next(State current, Board board, Direction direction) {
//        board.changePlayingState(current);
//        board.move(direction);
//        return board.getState();
//    }

    public List<Box> getBoxes() {
        return boxes;
    }

    public Player getPlayer() {
        return player;
    }
}
