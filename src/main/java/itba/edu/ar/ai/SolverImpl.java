package itba.edu.ar.ai;

import itba.edu.ar.ai.heuristics.MyHeutistic;
import itba.edu.ar.api.*;
import itba.edu.ar.model.Board;
import itba.edu.ar.model.Direction;
import itba.edu.ar.model.State;

import static itba.edu.ar.api.SearchAlgorithm.*;

public class SolverImpl implements Solver {

    private static long nodes = 0;
    private Board board;
    private Storage frontier;
    private SearchAlgorithm algorithm;
    private Heuristic heuristic;

    public SolverImpl(Board board, SearchAlgorithm algorithm, Heuristic heuristic) {
        this.board = board;
        this.frontier = algorithm.getStorage();
        this.algorithm = algorithm;
        this.heuristic = heuristic;
    }

    @Override
    public void solve() {
        Node root = new Node.Builder(board.getInitialState()).build();
        frontier.add(root);

        while (!frontier.isEmpty()) {
            /* Quitamos un nodo de la frontera */
            Node node = frontier.get();


            /* Si es el estado es goal, encontramos una solucion conforme a nuestro algoritmo */
            if (board.isComplete(node.getState())) {
                System.out.println("Solution");
                printSolution(node);
                return;
            }

            /* Explotamos el nodo y generamos sus hijos */
            if (algorithm == IDDFS || algorithm == IDA_STAR) {
                if (node.getDepth() < ((IDStorage)frontier).getLimit()) {
                    explode(node);
                }
                /* Si estamos en IDDFS y la frontera esta vacia incrementamos el limite y hacemos reset */
                if (frontier.isEmpty()) {
                    ((IDStorage) frontier).deepend();
                }
            } else {
                explode(node);
            }
        }
    }

    /*
        Cualquier otro
        No regresar al estado padre.
        No crear rutas que tengan ciclos.
        No volver a expandir estados ya expandidos previamente.
        Al marcar los nodos ya explorados (visitados), aseguramos que no se regresasra al esto padre,
        ya que el padre ya fue visitado y que no se generen ciclos dado que nunca se explora a un mismo estado
    */
    /*
        IDD
        Explotamos siempre que estemos por debajo de la maxima profundidad
        No regresar al estado padre.
        No crear rutas que tengan ciclos.
        No volver a expandir estados ya expandidos previamente. Si existe una solución a profundidad N, podemos no encontrarla.
        Es conveniente expandir los nodos explorados antes, siempre evitando que se formen ciclos o regresar al estado padre.
        Caso padre, es facil fijarse con el nodo que este arriba.
        Caso ciclos, no queda otra que revisar la branch
    */
    private void explode(Node node) {
        for (Direction direction : board.getPosibleMovements(node.getState())) {
            State childState = board.move(node.getState(), direction);
            Node.Builder child = new Node.Builder(childState).withParent(node).withMovement(direction);
            if (heuristic != null) {
                child = child.withEvaluation(heuristic.evaluate(board, childState)).withCost(Cost.getCost(childState));
            }
            nodes++;
            frontier.add(child.build());
        }
    }

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
        Board board = Board.from("./src/main/resources/Levels/Level 10");

        System.out.println(board.print(board.getInitialState()));

        SolverImpl solver = new SolverImpl(board, IDDFS, new MyHeutistic());

        long start = System.currentTimeMillis();

        solver.solve();

        System.out.println("nodes:" + nodes);
        System.out.println("time:" + (System.currentTimeMillis() - start));
    }

}
