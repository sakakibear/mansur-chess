package game;

public enum Player {
    PLAYER_1(1),
    PLAYER_2(2);

    private final int id;

    private Player(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}