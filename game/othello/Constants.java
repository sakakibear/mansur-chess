package game.othello;

public final class Constants {

    private Constants() {
    }

    // Game
    public static final int BOARD_SIZE = 8;
    public static final int EMPTY = 0;
    public static final int DARK = 1;
    public static final int LIGHT = 2;
    public static final char[] DISCS = { ' ', '��', '��' };

    // Evaluation
    public static final int VALUE_WIN = 10000;
    public static final int VALUE_LOSE = -VALUE_WIN;
    public static final int VALUE_UPPER_BOUND = VALUE_WIN + 1;
    public static final int VALUE_LOWER_BOUND = -VALUE_UPPER_BOUND;
}