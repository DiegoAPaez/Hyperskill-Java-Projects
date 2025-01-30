package lastpencil;

public class Player {
    String name;
    boolean winningPosition = false;
    boolean startsFirst = false;

    public Player(String name, boolean startsFirst) {
        this.name = name;
        this.startsFirst = startsFirst;
    }
}
