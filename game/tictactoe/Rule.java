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
        // check if board is full
        boolean isBoardFull = true;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board.get(i, j) == 0)
                    isBoardFull = false;
            }
        }
        if (isBoardFull)
            return true;

        Player winner = getWinner(board);
        return winner == null ? false : true;
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
