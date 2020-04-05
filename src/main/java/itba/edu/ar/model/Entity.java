package itba.edu.ar.model;

public enum Entity {

    PLAYER('@'), BOX('$'), GOAL('.'), WALL('#'), TILE(' '), GOAL_BOX('*'), GOAL_PLAYER('+');

    private final char c;

    private Entity(char c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return String.valueOf(c);
    }

    public static Entity from(char c) {
        switch (c) {
            case '.':
                return GOAL;
            case '@':
                return PLAYER;
            case '$':
                return BOX;
            case '#':
                return WALL;
            case '*':
                return GOAL_BOX;
            case '+':
                return GOAL_PLAYER;
            default:
                return TILE;
        }
    }
}
