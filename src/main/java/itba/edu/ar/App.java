package itba.edu.ar;

import itba.edu.ar.ai.SolverImpl;
import itba.edu.ar.api.SearchAlgorithm;
import itba.edu.ar.api.Solver;
import itba.edu.ar.model.Board;
import org.kohsuke.args4j.Option;

import java.io.IOException;

public class App {

    @Option(name = "-Da", usage = "Algoritmo con el cual se realizara la busqueda")
    private SearchAlgorithm algorithm;

    @Option(name = "-Dl", usage = "Archivo con la descripcion del nivel")
    private String levelFilename;

    @Option(name = "-Dh", usage = "Heuristica a usar")
    private String heuristic;

    public static void main(String[] args) {
        App app = new App();

        try {
            CmdParserUtils.init(args, app);
        } catch (IOException e) {
            System.out.println("There was a problem reading the arguments");
            System.exit(1);
        }

        Board board = Board.from(app.levelFilename);
        Solver solver = new SolverImpl(board, app.algorithm, null);
        solver.solve();
    }
}
