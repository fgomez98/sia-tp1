package itba.edu.ar.ai;

import itba.edu.ar.api.SearchAlgorithm;
import itba.edu.ar.api.Solver;
import itba.edu.ar.api.Storage;
import itba.edu.ar.model.Direction;
import itba.edu.ar.model2.Board;
import itba.edu.ar.model2.State;

import java.util.*;

import static itba.edu.ar.api.SearchAlgorithm.DFS;

public class SolverImpl implements Solver {

    private Board board;
    private Node root;
    private Storage frontier;
    private Set<Integer> explored;

    public SolverImpl(Board board, SearchAlgorithm algorithm) {
        this.board = board;
        this.frontier = algorithm.getStorage();
        this.explored = new HashSet<>();
    }

    /*
        Por como esta es valido para los metodos no informados
     */
    @Override
    public void solve() {
        root = new Node(board.getState(),new LinkedList<>(), 0);
        frontier.push(root);

        while (!frontier.isEmpty()) {
            Node node = frontier.pop();
            explored.add(node.hashCode()); /* Lo marcamos como visto */
            board.changePlayingState(node.getState());
            printSolution(node);
            if (board.isComplete()) {
                printSolution(node);
                return;
            }
            /* Explotamos el nodo y generamos sus hijos */
            List<Node> children = explode(node);
            /* Agregamos a la frontera segun el metodo que corresponda */
            for (Node child : children) {
                if (!explored.contains(child.hashCode())) {
                    frontier.push(child);
                }
            }
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

    private void printSolution(Node node) {
        node.getMovements().forEach(m -> System.out.print(m.name() + ", "));
        System.out.println();
//        board.changePlayingState(node.getState());
        System.out.println(board);
    }

    public static void main(String args[]) {
        Board board = Board.from("./src/main/resources/Levels/Level 1");
        SolverImpl solver = new SolverImpl(board, DFS);
        solver.solve();
        System.out.println(solver.frontier.isEmpty());
    }
}
