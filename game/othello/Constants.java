package game.othello;

import static game.Constants.VALUE_UPPER_BOUND;

public final class Constants {

    private Constants() {
    }

    // Game
    public static final int BOARD_SIZE = 8;
    public static final int EMPTY = 0;
    public static final int DARK = 1;
    public static final int LIGHT = 2;
    public static final char[] DISCS = { ' ', '●', '○' };

    // Evaluation
    public static final int VALUE_WIN = VALUE_UPPER_BOUND - 1;
    public static final int VALUE_LOSE = -VALUE_WIN;
    public static final int CORNER_SCORE = 5;
    public static final int STABLE_SCORE = 3;
    public static final int UNSTABLE_X_SCORE = -5;
}
