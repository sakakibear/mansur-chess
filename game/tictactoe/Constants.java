package game.tictactoe;

public final class Constants {

    private Constants() {
    }

    // Game
    public static final int BOARD_SIZE = 3;
    public static final int DEFAULT_SEARCH_DEPTH = 5;
    public static final int PLAYER_1 = 1;
    public static final int PLAYER_2 = 2;
    public static final char[] PIECES = { ' ', 'O', 'X' };

    // Evaluation
    public static final int VALUE_WIN = 1000;
    public static final int VALUE_LOSE = -VALUE_WIN;
    public static final int VALUE_UPPER_BOUND = VALUE_WIN + 1;
    public static final int VALUE_LOWER_BOUND = -VALUE_UPPER_BOUND;
    public static final int VALUE_ONE_IN_LINE = 1;
    public static final int VALUE_TWO_IN_LINE = 10;
}