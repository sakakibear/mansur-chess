package game.tictactoe;

import static game.tictactoe.Constants.BOARD_SIZE;
import static game.tictactoe.Constants.PLAYER_1;
import static game.tictactoe.Constants.PLAYER_2;
import static game.tictactoe.Constants.VALUE_LOSE;
import static game.tictactoe.Constants.VALUE_ONE_IN_LINE;
import static game.tictactoe.Constants.VALUE_TWO_IN_LINE;
import static game.tictactoe.Constants.VALUE_WIN;

import game.BaseBoard;
import game.BaseEvaluator;

public class Evaluator extends BaseEvaluator {

    @Override
    public int evaluate(BaseBoard board) {
        // TODO Could be solved using generic
        Board b = (Board) board;
        int value = 0;
        // check the rows
        for (int i = 0; i < BOARD_SIZE; i++) {
            int[] line = new int[BOARD_SIZE];
            for (int j = 0; j < BOARD_SIZE; j++) {
                line[j] = b.get(i, j);
            }
            int v = getValueOfLine(line);
            if (v == VALUE_WIN || v == VALUE_LOSE)
                return v;
            value += v;
        }
        // check the columns
        for (int j = 0; j < BOARD_SIZE; j++) {
            int[] line = new int[BOARD_SIZE];
            for (int i = 0; i < BOARD_SIZE; i++) {
                line[i] = b.get(i, j);
            }
            int v = getValueOfLine(line);
            if (v == VALUE_WIN || v == VALUE_LOSE)
                return v;
            value += v;
        }
        // check the diagonal lines
        int[] line = new int[BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            line[i] = b.get(i, i);
        }
        int v = getValueOfLine(line);
        if (v == VALUE_WIN || v == VALUE_LOSE)
            return v;
        value += v;
        for (int i = 0; i < BOARD_SIZE; i++) {
            line[i] = b.get(i, BOARD_SIZE - i - 1);
        }
        v = getValueOfLine(line);
        if (v == VALUE_WIN || v == VALUE_LOSE)
            return v;
        value += v;
        return value;
    }

    /**
     * Get the evaluation result of a certain line of the board. Could be
     * horizontal, vertical or diagonal since the rules are the same.
     * 
     * @param line
     * @return value
     */
    private int getValueOfLine(int[] line) {
        int p1 = 0, p2 = 0;
        for (int n : line) {
            if (n == PLAYER_1)
                p1++;
            if (n == PLAYER_2)
                p2++;
        }
        if (p1 > 0 && p2 > 0)
            return 0;
        // Three in a line means winning
        if (p1 == 3)
            return VALUE_WIN;
        if (p1 == 2)
            return VALUE_TWO_IN_LINE;
        if (p1 == 1)
            return VALUE_ONE_IN_LINE;
        // Three in a line of the other player means losing
        if (p2 == 3)
            return VALUE_LOSE;
        if (p2 == 2)
            return -VALUE_TWO_IN_LINE;
        if (p2 == 1)
            return -VALUE_ONE_IN_LINE;
        return 0;
    }
}
