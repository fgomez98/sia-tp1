package itba.edu.ar.model;


public class HeuristicCoordinate extends Coordinate {

    private int points;

    private HeuristicCoordinate(int x, int y, int points) {
        super(x, y);
        this.points = points;
    }

    public static HeuristicCoordinate from(int x, int y, int points) {
        return new HeuristicCoordinate(x, y, points);
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
