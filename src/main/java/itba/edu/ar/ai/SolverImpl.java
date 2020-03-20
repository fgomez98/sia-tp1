package itba.edu.ar.ai;

import itba.edu.ar.api.SearchAlgorithm;
import itba.edu.ar.api.Solver;
import itba.edu.ar.api.Storage;
import itba.edu.ar.model.Board;
import itba.edu.ar.model.Coordinate;
import itba.edu.ar.model.Direction;
import itba.edu.ar.model.State;

import java.util.*;

import static itba.edu.ar.api.SearchAlgorithm.BFS;
import static itba.edu.ar.api.SearchAlgorithm.DFS;

public class SolverImpl implements Solver {

    private static long nodes = 0;
    private static long explodedNodes = 0;
    private Board board;
    private Node root;
    private Storage frontier;
    private Set<State> explored;

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
        root = new Node(board.getInitialState(), 0, null);
        frontier.add(root);

        while (!frontier.isEmpty()) {
            Node node = frontier.get();
//            printSolution(node, board, false);
            explored.add(node.getState()); /* Lo marcamos como visto */
            if (board.isComplete(node.getState())) {
                System.out.println("Solution");
                printSolution(node);
                return;
            }
            /* Explotamos el nodo y generamos sus hijos */
            explode(node);
        }
    }

    private void explode(Node node) {
        for (Direction direction : board.getPosibleMovements(node.getState())) {
            explodedNodes++;
            State childState = board.move(node.getState(), direction);
            Node child = new Node(childState, node.getMovements(), node.getDepth() + 1, node);
            child.addMovement(direction);
            if (!explored.contains(child.getState())) {
                nodes++;
                frontier.add(child);
            } else {
//                printSolution(child, board, true);
            }
        }
    }


    /*

    private boolean hasCicle(Node parent, Node node) {
        if (parent == null) {
             llegamos al root del arbol
            return false;
        }
        return parent.hashCode() == node.hashCode() || hasCicle(parent.getParent(), node);
    }

    */


    private void printSolution(Node node) {
        node.getMovements().forEach(m -> System.out.print(m.name() + ", "));
        System.out.println();
        System.out.println(board.print(node.getState()));
    }

    private static void printSolution(Node node, Board board, boolean visited) {
        String color = "";
        String colorReset = "";
        if (visited) {
            color = "\u001B[31m";
            colorReset = "\u001B[0m";
        }
        System.out.println(color);
        node.getMovements().forEach(m -> System.out.print(m.name() + ", "));
        System.out.println();
        System.out.println(board.print(node.getState()));
        System.out.println(colorReset);
    }

    public static void main(String[] args) {
        Board board = Board.from("./src/main/resources/Levels/Level 4");
        SolverImpl solver = new SolverImpl(board, DFS);

        long start = System.currentTimeMillis();

        solver.solve();

        System.out.println("exploted:" + explodedNodes);
        System.out.println("nodes:" + nodes);
        System.out.println("time:" + (System.currentTimeMillis() - start));

    }
}
