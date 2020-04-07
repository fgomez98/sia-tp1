package itba.edu.ar;

import itba.edu.ar.ai.Node;
import itba.edu.ar.ai.SolverImpl;
import itba.edu.ar.api.SearchAlgorithm;
import itba.edu.ar.api.Solver;
import itba.edu.ar.model.Board;
import itba.edu.ar.model.Either;
import itba.edu.ar.model.Heuristics;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;

import java.io.IOException;

import static itba.edu.ar.api.SearchAlgorithm.*;

public class App {

    @Option(name = "-algorithm", usage = "Algoritmo con el cual se realizara la busqueda", required = true)
    private SearchAlgorithm algorithm;

    @Option(name = "-level", usage = "Archivo con la descripcion del nivel", required = true, handler = StringArrayOptionHandler.class)
    private String[] levelFilename;

    public String getLevelFilename() {
        StringBuilder sb = new StringBuilder();
        for (String s : levelFilename) {
            sb.append(s.replaceAll("\\\\", ""));
            sb.append(" ");
        }
        return sb.toString().trim();
    }

    @Option(name = "-heuristic", usage = "Heuristica a usar")
    private Heuristics heuristic;

    @Option(name = "-out", usage = "Directorio donde se guardara en un archivo .txt los resultados", handler = StringArrayOptionHandler.class)
    private String[] outFilename = {"./"};

    public String getOutFilename() {
        StringBuilder sb = new StringBuilder();
        for (String s : outFilename) {
            sb.append(s.replaceAll("\\\\", ""));
            sb.append(" ");
        }
        return sb.toString().trim();
    }

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

        Board board = Board.from(app.getLevelFilename());

        SearchAlgorithm.resetTree = app.resetTree;

        if (board.getInitialState().getBoxes().size() >= 5) {
            if (app.heuristic == Heuristics.MANHATTAN_OPT) {
                System.out.println("Como el numero de cajas es mayor a 5, la heuristica se cambia a MANHATTAN");
                app.heuristic = Heuristics.MANHATTAN;
            } else if (app.heuristic == Heuristics.POINT_POSITION_OPT) {
                System.out.println("Como el numero de cajas es mayor a 5, la heuristica se cambia a POINT_POSITION");
                app.heuristic = Heuristics.POINT_POSITION;
            }
        }

        Solver solver = new SolverImpl(board, app.algorithm, app.heuristic, app.timeLimit, !app.deadlocksOff);

        Either<Node, Boolean> solution = solver.solve();

        if (solution.isValuePresent()) {
            System.out.println("Solución encontrada");
        } else {
            if (solution.getAlternative())
                System.out.println("La aplicación llegó al timeout especificado. Intente correr con un timeout mas alto o intentá cambiar el algoritmo");
            else
                System.out.println("La aplicación no encontró una solución");
        }

        try {
            StringBuilder sb = new StringBuilder(app.getOutFilename());
            sb.append('/').append(app.algorithm.name());
            if (app.heuristic != null) {
                sb.append("_").append(app.heuristic.name());
            }
            sb.append(".txt");
            solver.outputMovements(solution, sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
