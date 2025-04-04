package game.othello;

import static game.Constants.PLAYER_1;
import static game.Constants.PLAYER_2;
import static game.othello.Constants.BOARD_SIZE;
import static game.othello.Constants.CORNER_SCORE;
import static game.othello.Constants.DARK;
import static game.othello.Constants.EMPTY;
import static game.othello.Constants.LIGHT;
import static game.othello.Constants.STABLE_SCORE;
import static game.othello.Constants.UNSTABLE_X_SCORE;
import static game.othello.Constants.VALUE_LOSE;
import static game.othello.Constants.VALUE_WIN;

import game.BaseEvaluator;

public class Evaluator extends BaseEvaluator<Board> {

    protected Rule rule = Rule.getInstance();

    @Override
    public int evaluate(Board board) {
        GameStatus gameStatus = rule.getGameStatus(board);
        if (gameStatus.getIsGameOver()) {
            int winnerPlayer = gameStatus.getWinnerPlayer();
            if (winnerPlayer == PLAYER_1)
                return VALUE_WIN;
            else if (winnerPlayer == PLAYER_2)
                return VALUE_LOSE;
            else
                return 0;
        }

        int darkCnt = gameStatus.getDarkCnt(), lightCnt = gameStatus.getLightCnt();
        int stableAdjustment = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                int disc = board.get(i, j);
                int pt = isStable(board, i, j) ? STABLE_SCORE : 0;
                if (disc == LIGHT)
                    pt *= -1;
                stableAdjustment += pt;
            }
        }
        return darkCnt - lightCnt + stableAdjustment + getCornerAdjustment(board);
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

        // Return cached value only if cache is true
        if (board.getStableCache(m, n))
            return true;

        boolean result = false;
        if (m == 0 || m == BOARD_SIZE - 1 || n == 0 || n == BOARD_SIZE - 1)
            result = isStableOnEdge(board, m, n);
        else
            result = isStableAllDirections(board, m, n);
        if (result)
            board.setStableCache(m, n, true);
        return result;
    }

    protected boolean isStableOnEdge(Board board, int m, int n) {
        // Corner
        if ((m == 0 || m == BOARD_SIZE - 1) && (n == 0 || n == BOARD_SIZE - 1))
            return true;

        int iStart, jStart;
        int[] dir;
        if (m == 0 || m == BOARD_SIZE - 1) {
            iStart = m;
            jStart = 0;
            dir = new int[]{0, 1};
        } else if (n == 0 || n == BOARD_SIZE - 1) {
            iStart = 0;
            jStart = n;
            dir = new int[]{1, 0};
        } else {
            // Unexpected: not on edge
            return false;
        }

        boolean flag = true;
        // Check if the edge is all filled
        for (int i = iStart, j = jStart; i < BOARD_SIZE && j < BOARD_SIZE; i += dir[0], j += dir[1]) {
            if (board.get(i, j) == EMPTY) {
                flag = false;
                break;
            }
        }
        if (flag)
            return true;

        // Check if all discs are the same colour on either direction
        int disc = board.get(m, n);
        flag = true;
        for (int i = m, j = n; i >= 0 && j >= 0; i -= dir[0], j -= dir[1]) {
            if (disc != board.get(i, j)) {
                flag = false;
                break;
            }
        }
        if (flag)
            return true;
        // The other direction
        flag = true;
        for (int i = m, j = n; i < BOARD_SIZE && j < BOARD_SIZE; i += dir[0], j += dir[1]) {
            if (disc != board.get(i, j)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    protected boolean isStableAllDirections(Board board, int m, int n) {
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
