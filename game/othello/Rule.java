package game.othello;

import static game.Constants.PLAYER_1;
import static game.Constants.PLAYER_2;
import static game.othello.Constants.BOARD_SIZE;
import static game.othello.Constants.DARK;
import static game.othello.Constants.EMPTY;
import static game.othello.Constants.LIGHT;

import java.util.ArrayList;
import java.util.List;

/**
 * Rule of Othello game.
 */
public class Rule {
    private static Rule instance;

    private Rule() {
    }

    public static Rule getInstance() {
        if (instance == null)
            instance = new Rule();
        return instance;
    }

    // 8 directions on the board
    protected static int[][] dirs = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 },
            { 1, 1 }, };

    protected boolean isValidMove(Board board, int player, int x, int y) {
        if (x < 0 || x >= BOARD_SIZE || y < 0 || y >= BOARD_SIZE)
            return false;
        if (board.get(x, y) != EMPTY)
            return false;
        int self = player == PLAYER_1 ? DARK : LIGHT;
        int oppo = player == PLAYER_1 ? LIGHT : DARK;
        for (int[] dir : dirs) {
            int i = x + dir[0], j = y + dir[1];
            boolean hasOppo = false;
            while (i >= 0 && i < BOARD_SIZE && j >= 0 && j < BOARD_SIZE && board.get(i, j) == oppo) {
                hasOppo = true;
                i += dir[0];
                j += dir[1];
            }
            if (hasOppo && i >= 0 && i < BOARD_SIZE && j >= 0 && j < BOARD_SIZE && board.get(i, j) == self)
                return true;
        }
        return false;
    }

    public List<Move> getMoves(Board board, int player) {
        // return empty list if game over
        if (isGameOver(board)) {
            return new ArrayList<Move>();
        }
        List<Move> result = getNoPassMoves(board, player);
        // Add a pass move if no moves could be done
        if (result.size() == 0)
            result.add(new Move(player));
        return result;
    }

    public List<Move> getNoPassMoves(Board board, int player) {
        List<Move> result = new ArrayList<Move>();
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                if (isValidMove(board, player, i, j))
                    result.add(new Move(i, j, player));
        return result;
    }

    public void takeMove(Board board, Move move) {
        if (move.isPass())
            return;
        int player = move.getPlayer();
        int x = move.getX();
        int y = move.getY();
        int self = player == PLAYER_1 ? DARK : LIGHT;
        int oppo = player == PLAYER_1 ? LIGHT : DARK;
        board.set(x, y, self);
        for (int[] dir : dirs) {
            int i = x + dir[0], j = y + dir[1];
            int cnt = 0;
            while (i >= 0 && i < BOARD_SIZE && j >= 0 && j < BOARD_SIZE && board.get(i, j) == oppo) {
                cnt++;
                i += dir[0];
                j += dir[1];
            }
            if (i >= 0 && i < BOARD_SIZE && j >= 0 && j < BOARD_SIZE && board.get(i, j) == self) {
                int ii = x + dir[0], jj = y + dir[1];
                for (int k = 0; k < cnt; k++) {
                    board.set(ii, jj, self);
                    ii += dir[0];
                    jj += dir[1];
                }
            }
        }
    }

    public boolean isGameOver(Board board) {
        return getNoPassMoves(board, PLAYER_1).isEmpty() && getNoPassMoves(board, PLAYER_2).isEmpty();
    }
}
