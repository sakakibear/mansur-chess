package game.othello;

import static game.othello.Constants.BOARD_SIZE;
import static game.othello.Constants.CORNER_SCORE;
import static game.othello.Constants.DARK;
import static game.othello.Constants.EMPTY;
import static game.othello.Constants.LIGHT;
import static game.othello.Constants.STABLE_SCORE;
import static game.othello.Constants.UNSTABLE_X_SCORE;
import static game.othello.Constants.VALUE_LOSE;
import static game.othello.Constants.VALUE_WIN;

import game.BaseBoard;
import game.BaseEvaluator;

public class Evaluator extends BaseEvaluator {

    protected Rule rule = Rule.getInstance();

    @Override
    public int evaluate(BaseBoard board) {
        // TODO Could be solved using generic
        Board b = (Board) board;
        boolean isGameOver = rule.isGameOver(b);
        int darkCnt = 0, lightCnt = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                int disc = b.get(i, j);
                if (disc == DARK)
                    darkCnt++;
                else if (disc == LIGHT)
                    lightCnt++;
            }
        }
        if (isGameOver) {
            if (darkCnt > lightCnt)
                return VALUE_WIN;
            else if (darkCnt == lightCnt)
                return 0;
            else
                return VALUE_LOSE;
        }
        // Calculate additional scores of stable discs
        int stableAdjustment = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                int disc = b.get(i, j);
                int pt = isStable(b, i, j) ? STABLE_SCORE : 0;
                if (disc == LIGHT)
                    pt *= -1;
                stableAdjustment += pt;
            }
        }
        return darkCnt - lightCnt + stableAdjustment + getCornerAdjustment(b);
    }

    protected int getCornerAdjustment(Board board) {
        int res = 0;
        res += getCornerValue(board.get(0, 0), board.get(1, 1));
        res += getCornerValue(board.get(0, 7), board.get(1, 6));
        res += getCornerValue(board.get(7, 0), board.get(6, 1));
        res += getCornerValue(board.get(7, 7), board.get(6, 6));
        return res;
    }

    protected int getCornerValue(int corner, int x) {
        if (corner == EMPTY) {
            if (x == DARK)
                return UNSTABLE_X_SCORE;
            else if (x == LIGHT)
                return -UNSTABLE_X_SCORE;
            return 0;
        }
        if (corner == DARK)
            return CORNER_SCORE;
        else
            return -CORNER_SCORE;
    }

    protected boolean isStable(Board board, int m, int n) {
        int disc = board.get(m, n);
        if (disc == EMPTY)
            return false;

        // At corner
        if ((m == 0 || m == BOARD_SIZE - 1) && (n == 0 || n == BOARD_SIZE - 1)) {
            return true;
        }

        // On an edge
        if (m == 0 || m == BOARD_SIZE - 1) {
            // The edge is all filled
            boolean flag = true;
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board.get(m, j) == EMPTY) {
                    flag = false;
                    break;
                }
            }
            if (flag)
                return true;
            // All the same colour to corner on either direction
            flag = true;
            for (int j = 0; j < n; j++) {
                if (disc != board.get(m, j)) {
                    flag = false;
                    break;
                }
            }
            if (flag)
                return true;
            flag = true;
            for (int j = n + 1; j < BOARD_SIZE; j++) {
                if (disc != board.get(m, j)) {
                    flag = false;
                    break;
                }
            }
            if (flag)
                return true;
        } else if (n == 0 || n == BOARD_SIZE - 1) {
            // The edge is all filled
            boolean flag = true;
            for (int i = 0; i < BOARD_SIZE; i++) {
                if (board.get(i, n) == EMPTY) {
                    flag = false;
                    break;
                }
            }
            if (flag)
                return true;
            // All the same colour to corner on either direction
            flag = true;
            for (int i = 0; i < m; i++) {
                if (disc != board.get(i, n)) {
                    flag = false;
                    break;
                }
            }
            if (flag)
                return true;
            flag = true;
            for (int i = m + 1; i < BOARD_SIZE; i++) {
                if (disc != board.get(i, n)) {
                    flag = false;
                    break;
                }
            }
            if (flag)
                return true;
        }

        // If all 8 directions are filled
        // False does not exactly mean it's unstable
        int[][] dirs = { {0, -1}, {-1, -1}, {-1, 0}, {-1, 1} };
        for (int[] dir : dirs) {
            for (int i = m, j = n; i >= 0 && i < BOARD_SIZE && j >= 0 && j < BOARD_SIZE; i += dir[0], j += dir[1]) {
                if (board.get(i, j) == EMPTY)
                    return false;
            }
            for (int i = m, j = n; i >= 0 && i < BOARD_SIZE && j >= 0 && j < BOARD_SIZE; i -= dir[0], j -= dir[1]) {
                if (board.get(i, j) == EMPTY)
                    return false;
            }
        }
        return true;
    }
}
