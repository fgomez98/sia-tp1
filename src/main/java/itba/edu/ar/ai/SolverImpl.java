package itba.edu.ar.ai;

import itba.edu.ar.api.*;
import itba.edu.ar.model.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Queue;

import static itba.edu.ar.api.SearchAlgorithm.*;

public class SolverImpl implements Solver {

    private Board board;
    private Storage frontier;
    private SearchAlgorithm algorithm;
    private Heuristics heuristic;
    private boolean deadlocks;
    private long timebreak;

    public SolverImpl(Board board, SearchAlgorithm algorithm, Heuristics heuristic, long timebreak, boolean deadlocks) {
        this.board = board;
        this.frontier = algorithm.getStorage();
        this.algorithm = algorithm;
        this.heuristic = heuristic;
        this.deadlocks = deadlocks;
        this.timebreak = timebreak;
    }

    @Override
    public Either<Node, Boolean> solve() {
        Benchmarking.start = System.currentTimeMillis();
        Node.Builder root = new Node.Builder(board.getInitialState()).withCost(0);
        if (heuristic != null) {
            root = root.withEvaluation(heuristic.getEvaluate().apply(board, board.getInitialState()));
        }
        frontier.add(root.build());

        while (!frontier.isEmpty() && (System.currentTimeMillis() - Benchmarking.start) < timebreak) {
            /* Quitamos un nodo de la frontera */
            Node node = frontier.get();

            /* Si es el estado es goal, encontramos una solucion conforme a nuestro algoritmo */
            if (board.isComplete(node.getState())) {
                Benchmarking.nodesFrontier = frontier.frontierSize();
                Benchmarking.nodesExplorados = frontier.exploredSize();
                Benchmarking.end = System.currentTimeMillis();
                return Either.value(node);
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
        Benchmarking.nodesFrontier = frontier.frontierSize();
        Benchmarking.nodesExplorados = frontier.exploredSize();
        Benchmarking.end = System.currentTimeMillis();
        if ((System.currentTimeMillis() - Benchmarking.start) > timebreak) {
            return Either.alternative(true);
        }
        return Either.alternative(false);
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
        List<Direction> movments = board.getPosibleMovements(node.getState(), deadlocks);
        if (!movments.isEmpty()) {
            Benchmarking.nodesExploted++;
        }
        for (Direction direction : movments) {
            State childState = board.move(node.getState(), direction);
            Node.Builder child = new Node.Builder(childState)
                    .withParent(node)
                    .withMovement(direction)
                    .withCost(node.getGn() + Cost.getCost(childState));
            if (heuristic != null) {
                child = child.withEvaluation(heuristic.getEvaluate().apply(board, childState));
            }
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

    public void outputMovements(Either<Node, Boolean> either, String fileName) throws IOException {
        Writer writer = new FileWriter(fileName);

        StringBuilder sb = new StringBuilder();

        sb.append("Algoritmo de Busqueda: ").append(this.algorithm.name()).append('\n');

        if (this.heuristic != null) {
            sb.append("Heuristica: ").append(this.heuristic.name()).append('\n');
        } else {
            sb.append("Heuristica: None").append('\n');
        }

        sb.append("Tiempo: ").append(Benchmarking.getSimTime()).append(" seg").append('\n');

        if (either.isValuePresent()) {
            Node node = either.getValue();
            sb.append("Costo: ").append(node.getGn()).append('\n');
            sb.append("Depth: ").append(node.getDepth()).append('\n');
        }

        sb.append("Nodos explotados: ").append(Benchmarking.nodesExploted).append('\n');
        sb.append("Nodos explorados (sin repetir): ").append(Benchmarking.nodesExplorados).append('\n');
        sb.append("Nodos frontera: ").append(Benchmarking.nodesFrontier).append('\n');

        if (either.isValuePresent()) {
            Node node = either.getValue();
            sb.append("Movimientos: ");
            for (Direction movement : node.getMovements()) {
                sb.append(movement.name()).append(", ");
            }
            sb.append('\n');
        }

        writer.write(sb.toString());

        State currentState = board.getInitialState();
        writer.write(board.print(currentState));

        if (either.isValuePresent()) {
            Node node = either.getValue();
            Queue<Direction> movements = node.getMovements();
            while (!movements.isEmpty()) {
                Direction direction = movements.poll();
                currentState = board.move(currentState, direction);
                writer.write(direction.name());
                writer.write('\n');
                writer.write(board.print(currentState));
            }
        }

        writer.close();
    }

    public static void main(String[] args) {
        Board board = Board.from("./src/main/resources/Levels/Level 9");

        System.out.println(board.print(board.getInitialState()));
        System.out.println(board.printDeadBoxes());

        Heuristics heuristics = Heuristics.POINT_POSITION;
        if (board.getInitialState().getBoxes().size() > 5) {
            if (heuristics == Heuristics.MANHATTAN_OPT)
                heuristics = Heuristics.MANHATTAN;
            else if (heuristics == Heuristics.POINT_POSITION_OPT)
                heuristics = Heuristics.POINT_POSITION;
        }

        SolverImpl solver = new SolverImpl(board, IDA_STAR, heuristics, Long.MAX_VALUE, true);


        Either<Node, Boolean> solution = solver.solve();
        try {
            solver.outputMovements(solution, "./src/main/resources/Solutions/" + solver.algorithm.name() + "_" + solver.heuristic.name() + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (solution.isValuePresent()) {
            System.out.println("Solution");
            solver.printSolution(solution.getValue());
            System.out.println("nodes:" + Benchmarking.nodesExploted);
            System.out.println("time:" + Benchmarking.getSimTime());
        } else {
            if (solution.getAlternative())
                System.out.println("There was a timeout. Try to solve this level again using another algorithm");
            else
                System.out.println("No solution was found");

        }
    }

}
