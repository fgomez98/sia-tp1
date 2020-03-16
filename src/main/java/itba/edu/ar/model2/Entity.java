package itba.edu.ar.model2;

public enum Entity {

    PLAYER('@'), BOX('$'), GOAL('.'), WALL('#'), TILE(' ');

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
            default:
                return TILE;
        }
    }
}
