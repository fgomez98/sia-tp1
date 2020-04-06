package itba.edu.ar;

import itba.edu.ar.ai.Node;
import itba.edu.ar.ai.SolverImpl;
import itba.edu.ar.api.SearchAlgorithm;
import itba.edu.ar.api.Solver;
import itba.edu.ar.model.Board;
import itba.edu.ar.model.Heuristics;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.IOException;
import java.util.Optional;

import static itba.edu.ar.api.SearchAlgorithm.*;

public class App {

    @Option(name = "-algorithm", usage = "Algoritmo con el cual se realizara la busqueda", required = true)
    private SearchAlgorithm algorithm;

    @Option(name = "-level", usage = "Archivo con la descripcion del nivel", required = true)
    private String levelFilename;

    @Option(name = "-heuristic", usage = "Heuristica a usar")
    private Heuristics heuristic;

    @Option(name = "-out", usage = "Directorio donde se guardara en un archivo .txt los resultados")
    private String outFilename = "./";

    @Option(name = "-reset-tree", usage = "Reseteo del arbol en algoritmos Iterative Deepening")
    private boolean resetTree = false;

    @Option(name = "-deadlocks-off", usage = "Apaga chequeo de deadlocks")
    private boolean deadlocksOff = false;

    @Option(name = "-time", usage = "Tiempo limite a correr en segundos")
    public void setTimeLimit(long timeLimit) {
        this.timeLimit = timeLimit * 1000; /* A milisegundos */
    }

    private long timeLimit = Long.MAX_VALUE;

    public static void main(String[] args) {
        App app = new App();
        final CmdLineParser parser = new CmdLineParser(app);
        if (args.length < 1) {
            parser.printUsage(System.err);
            System.exit(1);
        }
        try {
            parser.parseArgument(args);
            if ((app.algorithm == GLOBAL_GREEDY || app.algorithm == A_STAR || app.algorithm == IDA_STAR) && app.heuristic == null) {
                throw new CmdLineException("Se necesita especificar una heuristica para el algoritmo seleccionado");
            }
        } catch (CmdLineException e) {
            System.out.println(e.getMessage());
            parser.printUsage(System.err);
            System.exit(1);
        }

        Board board = Board.from(app.levelFilename);

        SearchAlgorithm.resetTree = app.resetTree;

        Solver solver = new SolverImpl(board, app.algorithm, app.heuristic, app.timeLimit,!app.deadlocksOff);

        Optional<Node> solution = solver.solve();

        if (solution.isPresent()) {
            System.out.println("Solution");
            try {
                solver.outputMovments(solution.get(), app.outFilename + app.algorithm.name() + "_" + app.heuristic.name() + ".txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No solution");
        }
    }
}
