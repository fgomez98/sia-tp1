package itba.edu.ar.ai;

import itba.edu.ar.api.SearchAlgorithm;
import itba.edu.ar.api.Solver;
import itba.edu.ar.api.Storage;
import itba.edu.ar.model.Board;
import itba.edu.ar.model.Direction;
import itba.edu.ar.model.State;

import java.util.*;

import static itba.edu.ar.api.SearchAlgorithm.DFS;

public class SolverImpl implements Solver {

    private static long nodes = 0;
    private static long explodedNodes = 0;
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
        root = new Node(board.getInitialState(), 0);
        frontier.push(root);

        while (!frontier.isEmpty()) {
            Node node = frontier.pop();
            explored.add(node.hashCode()); /* Lo marcamos como visto */
            if (board.isComplete(node.getState())) {
                printSolution(node);
                return;
            }
            /* Explotamos el nodo y generamos sus hijos */
            List<Node> children = explode(node);
            for (Node child : children) {
                explodedNodes++;
                if (!explored.contains(child.hashCode())) {
                    frontier.push(child);
                    nodes++;
                }
            }
        }
    }

    private List<Node> explode(Node node) {
        List<Node> children = new LinkedList<>();
        for (Direction direction : board.getPosibleMovements(node.getState())) {
            State childState = board.move(node.getState(), direction);
            Node child = new Node(childState, node.getMovements(), node.getDepth() + 1);
            child.addMovement(direction);
            children.add(child);
            node.addChild(child, 1);
        }
        return children;
    }

    private void printSolution(Node node) {
        System.out.println(node.getMovements());
        System.out.println(board.print(node.getState()));
    }

    public static void main(String args[]) {
        Board board = Board.from("./src/main/resources/Levels/Level 1");
        SolverImpl solver = new SolverImpl(board, DFS);

        long start = System.currentTimeMillis();

        solver.solve();

        System.out.println("exploted:" + explodedNodes);
        System.out.println("nodes:" + nodes);
        System.out.println("time:" + (System.currentTimeMillis() - start) );

    }
}
