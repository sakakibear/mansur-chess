package game.othello;

import static game.othello.Constants.BOARD_SIZE;
import static game.othello.Constants.DARK;
import static game.othello.Constants.DISCS;
import static game.othello.Constants.EMPTY;
import static game.othello.Constants.LIGHT;

import game.BaseBoard;

/**
 * Chess board of Othello.
 */
/*-
 *   a b c d e f g h
 * 1
 * 2
 * 3
 * 4       ¡ð ¡ñ
 * 5       ¡ñ ¡ð
 * 6
 * 7
 * 8
 */
public class Board extends BaseBoard {

    protected int[][] board;

    @Override
    public BaseBoard clone() {
        Board cloned = new Board();
        for (int i = 0; i < board.length; i++)
            for (int j = 0; j < board[0].length; j++)
                cloned.board[i][j] = board[i][j];
        return cloned;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  a b c d e f g h\n");
        for (int i = 0; i < BOARD_SIZE; i++) {
            sb.append(String.format("%d ", i + 1));
            for (int j = 0; j < BOARD_SIZE; j++) {
                sb.append(String.format("%c ", DISCS[board[i][j]]));
            }
            sb.append(String.format("%d ", i + 1));
            sb.append("\n");
        }
        sb.append("  a b c d e f g h\n");
        return sb.toString();
    }

    public Board() {
        board = new int[BOARD_SIZE][BOARD_SIZE];
    }

    public int get(int i, int j) {
        return board[i][j];
    }

    public void set(int i, int j, int val) {
        board[i][j] = val;
    }

    public void init() {
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                board[i][j] = EMPTY;
        board[3][4] = board[4][3] = DARK;
        board[3][3] = board[4][4] = LIGHT;
    }
}
