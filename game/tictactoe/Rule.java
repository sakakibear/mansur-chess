package game.tictactoe;

import java.util.ArrayList;
import java.util.List;

import static game.tictactoe.Constants.BOARD_SIZE;

import game.GameRule;
import game.Player;

public class Rule implements GameRule<Move, Player, Board> {

    @Override
    public boolean isLegalMove(Board board, Move move) {
        return board.get(move.getX(), move.getY()) == 0;
    }

    @Override
    public List<Move> getLegalMoves(Board board, Player player) {
        List<Move> result = new ArrayList<Move>();
        if (isGameOver(board))
            return result;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board.get(i, j) == 0)
                    result.add(new Move(i, j, player));
            }
        }
        return result;
    }

    @Override
    public void makeMove(Board board, Move move) {
        board.set(move.getX(), move.getY(), move.getPlayer().getId());
    }

    @Override
    public boolean isGameOver(Board board) {
        // check rows
        for (int i = 0; i < BOARD_SIZE; i++) {
            boolean flag = true;
            int piece = board.get(i, 0);
            if (piece == 0) {
                flag = false;
                break;
            }
            for (int j = 1; j < BOARD_SIZE; j++) {
                if (piece != board.get(i, j)) {
                    flag = false;
                    break;
                }
            }
            if (flag)
                return true;
        }

        // check columns
        for (int j = 0; j < BOARD_SIZE; j++) {
            boolean flag = true;
            int piece = board.get(0, j);
            if (piece == 0) {
                flag = false;
                break;
            }
            for (int i = 1; i < BOARD_SIZE; i++) {
                if (piece != board.get(i, j)) {
                    flag = false;
                    break;
                }
            }
            if (flag)
                return true;
        }

        // check diagonal lines
        boolean flag = true;
        int piece = board.get(0, 0);
        if (piece == 0) {
            flag = false;
        }
        if (flag) {
            for (int i = 1; i < BOARD_SIZE; i++) {
                if (piece != board.get(i, i)) {
                    flag = false;
                    break;
                }
            }
        }
        if (flag)
            return true;
        flag = true;
        piece = board.get(0, BOARD_SIZE - 1);
        if (piece == 0) {
            flag = false;
        }
        if (flag) {
            for (int i = 1; i < BOARD_SIZE; i++) {
                if (piece != board.get(i, BOARD_SIZE - i - 1)) {
                    flag = false;
                    break;
                }
            }
        }
        if (flag)
            return flag;

        // check if board is full
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board.get(i, j) == 0)
                    return false;
            }
        }
        return true;
    }

    public Player getWinner(Board board) {
        int[] row = new int[BOARD_SIZE];
        Player rowWinner = null;

        // check horizontal rows
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                row[j] = board.get(i, j);
            }
            rowWinner = getWinnerOfRow(row);
            if (rowWinner != null)
                return rowWinner;
        }

        // check vertical rows
        for (int j = 0; j < BOARD_SIZE; j++) {
            for (int i = 0; i < BOARD_SIZE; i++) {
                row[i] = board.get(i, j);
            }
            rowWinner = getWinnerOfRow(row);
            if (rowWinner != null)
                return rowWinner;
        }

        // check diagonal rows
        for (int i = 0; i < BOARD_SIZE; i++) {
            row[i] = board.get(i, i);
        }
        if (rowWinner != null)
            return rowWinner;

        for (int i = 0; i < BOARD_SIZE; i++) {
            row[i] = board.get(i, BOARD_SIZE - i - 1);
        }
        rowWinner = getWinnerOfRow(row);
        if (rowWinner != null)
            return rowWinner;

        return null;
    }

    protected Player getWinnerOfRow(int[] row) {
        int cnt_player_1 = 0, cnt_player_2 = 0;
        for (int piece : row) {
            if (piece == Player.PLAYER_1.getId())
                cnt_player_1++;
            else if (piece == Player.PLAYER_2.getId())
                cnt_player_2++;
        }
        if (cnt_player_1 == BOARD_SIZE)
            return Player.PLAYER_1;
        if (cnt_player_2 == BOARD_SIZE)
            return Player.PLAYER_2;
        return null;
    }
}
