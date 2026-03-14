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
        boolean flag = true;
        for (int i = 0; i < BOARD_SIZE; i++) {
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
        }
        if (flag)
            return true;

        // check columns
        flag = true;
        for (int j = 0; j < BOARD_SIZE; j++) {
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
        }
        if (flag)
            return true;

        // check diagonal lines
        flag = true;
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
}
