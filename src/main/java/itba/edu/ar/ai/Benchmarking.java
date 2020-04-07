package itba.edu.ar.ai;

public class Benchmarking {

    public static long nodesExploted = 0;
    public static long nodesExplorados = 0;
    public static long nodesFrontier = 0;
    public static long start = 0;
    public static long end = 0;

    public static double getSimTime() {
        return (start > 0 && end > start) ? (end - start) / 1000.0 : -1.0;
    }
}
