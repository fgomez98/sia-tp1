package itba.edu.ar.ai;

import itba.edu.ar.model.Direction;
import itba.edu.ar.model2.Board;
import itba.edu.ar.model2.State;

import java.util.*;

public class SolverImpl implements Solver {

    private Board board;
    private Node root;
    private Queue<Node> frontier;
    private Set<Integer> explored;

    public SolverImpl(Board board) {
        this.board = board;
        this.frontier = new LinkedList<>();
        this.explored = new HashSet<>();
    }

    /*
        Por como esta es valido para los metodos no informados
     */
    @Override
    public void solve() {
        root = null;
        frontier.add(root);

        while (!frontier.isEmpty()) {
            Node node = frontier.poll();
            explored.add(node.hashCode()); /* Lo marcamos como visto */
            board.changePlayingState(node.getState());
            if (board.isComplete()) {
                return;
            }
            /* Explotamos el nodo y generamos sus hijos */
            List<Node> children = explode(node);
            /* Agregamos a la frontera segun el metodo que corresponda */
        }
    }

    public List<Node> explode(Node node) {
        List<Node> children = new LinkedList<>();
        for (Direction direction : board.getPosibleMovements()) {
            // aplicamos y sacamos nuevos estados
            // nuevo nodo y agregamos a la lista
            board.move(direction);
            State childState = board.getState();
            Queue<Direction> movements = new LinkedList<>(node.getMovements());
            movements.offer(direction);
            Node child = new Node(childState, movements, node.getDepth() + 1);
            children.add(child);
            node.addChild(child, 1);
            board.changePlayingState(node.getState());
        }
        return children;
    }
}
