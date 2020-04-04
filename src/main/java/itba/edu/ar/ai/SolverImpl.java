package itba.edu.ar.ai;

import itba.edu.ar.api.*;
import itba.edu.ar.model.Board;
import itba.edu.ar.model.Direction;
import itba.edu.ar.model.Heuristics;
import itba.edu.ar.model.State;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Optional;
import java.util.Queue;

import static itba.edu.ar.api.SearchAlgorithm.*;

public class SolverImpl implements Solver {

    private Board board;
    private Storage frontier;
    private SearchAlgorithm algorithm;
    private Heuristics heuristic;

    public SolverImpl(Board board, SearchAlgorithm algorithm, Heuristics heuristic) {
        this.board = board;
        this.frontier = algorithm.getStorage();
        this.algorithm = algorithm;
        this.heuristic = heuristic;
    }

    @Override
    public Optional<Node> solve() {
        Benchmarking.start = System.currentTimeMillis();
        Node.Builder root = new Node.Builder(board.getInitialState()).withCost(0);
        if (heuristic != null) {
            root = root.withEvaluation(heuristic.getEvaluate().apply(board, board.getInitialState()));
        }
        frontier.add(root.build());

        while (!frontier.isEmpty()) {
            /* Quitamos un nodo de la frontera */
            Node node = frontier.get();

            /* Si es el estado es goal, encontramos una solucion conforme a nuestro algoritmo */
            if (board.isComplete(node.getState())) {
                Benchmarking.end = System.currentTimeMillis();
                return Optional.of(node);
            }

            /* Explotamos el nodo y generamos sus hijos */
            if (algorithm == IDDFS || algorithm == IDA_STAR) {
                if (node.getDepth() < ((IDStorage) frontier).getLimit()) {
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
        Benchmarking.end = System.currentTimeMillis();
        return Optional.empty();
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
            Node.Builder child = new Node.Builder(childState)
                    .withParent(node)
                    .withMovement(direction)
                    .withCost(node.getCost() + Cost.getCost(childState));
            if (heuristic != null) {
                child = child.withEvaluation(heuristic.getEvaluate().apply(board, childState));
            }
            Benchmarking.nodesExploted++;
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

    public void outputMovments(Node node, String fileName) throws IOException {
        Writer writer = new FileWriter(fileName.toString());

        Queue<Direction> movements = node.getMovements();
        State currentState = board.getInitialState();

        StringBuilder sb = new StringBuilder();
        sb.append("Search Algorithm: ").append(this.algorithm.name()).append('\n');
        if (this.heuristic != null) {
            sb.append("Heuristic: ").append(this.heuristic.name()).append('\n');
        } else {
            sb.append("Heuristic: None").append('\n');
        }
        sb.append("Time: ").append(Benchmarking.getSimTime()).append(" sec").append('\n');
        sb.append("Cost: ").append(node.getCost()).append('\n');
        sb.append("Depth: ").append(node.getDepth()).append('\n');
        sb.append("Nodes exploted: ").append(Benchmarking.nodesExploted).append('\n');
        sb.append("Nodes fronteer: ").append(Benchmarking.nodesFronteer).append('\n');
        sb.append("Movements: ");
        for (Direction movement : node.getMovements()) {
            sb.append(movement.name()).append(", ");
        }
        sb.append('\n');

        writer.write(sb.toString());
        writer.write(board.print(currentState));

        while (!movements.isEmpty()) {
            Direction direction = movements.poll();
            currentState = board.move(currentState, direction);
            writer.write(direction.name());
            writer.write('\n');
            writer.write(board.print(currentState));
        }

        writer.close();
    }

    public static void main(String[] args) {
        Board board = Board.from("./src/main/resources/Levels/Level 3");

        System.out.println(board.print(board.getInitialState()));
        System.out.println(board.printDeadBoxes());

        SolverImpl solver = new SolverImpl(board, A_STAR, Heuristics.POINT_POSITION_OPT);

        Optional<Node> solution = solver.solve();

        solution.ifPresent(sol -> {
            try {
                solver.outputMovments(sol, "./src/main/resources/Solutions/" + solver.algorithm.name() + "_" + solver.heuristic.name() + ".txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        if (solution.isPresent()) {
            System.out.println("Solution");
            solver.printSolution(solution.get());
            System.out.println("nodes:" + Benchmarking.nodesExploted);
            System.out.println("time:" + Benchmarking.getSimTime());
        } else {
            System.out.println("No solution");
        }
    }

}
