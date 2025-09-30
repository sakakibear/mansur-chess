package game.tictactoe;

import static game.tictactoe.Constants.BOARD_SIZE;
import static game.tictactoe.Constants.PIECES;
import static game.tictactoe.Constants.VALUE_LOSE;
import static game.tictactoe.Constants.VALUE_WIN;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import game.BaseGame;
import game.Player;

public class TicTacToe extends BaseGame<Board, Move> {

    // Scanner to get user input
    protected Scanner scanner;

    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();
        game.run(args);
    }

    @Override
    public void init() {
        super.init();
        board = new Board();
        evaluator = new Evaluator();
        scanner = new Scanner(System.in);
    }

    @Override
    protected boolean isGameOver(Player player) {
        List<Move> moves = getValidMoves(player);
        if (moves.size() == 0)
            return true;
        int value = evaluate();
        if (value == VALUE_WIN || value == VALUE_LOSE)
            return true;
        return false;
    }

    @Override
    protected List<Move> getValidMoves(Player player) {
        List<Move> result = new ArrayList<Move>();
        int value = evaluate();
        if (value == VALUE_WIN || value == VALUE_LOSE)
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
    protected void move(Move move) {
        board.set(move.getX(), move.getY(), move.getPlayer().getId());
    }

    @Override
    protected Move getUserPlayerMove(Player curPlayer) {
        while (true) {
            System.out.printf("[%c] > ", PIECES[curPlayer.getId()]);
            String str = scanner.nextLine();
            str = str.trim();
            if (str.length() != 2)
                continue;
            // User input should look like 'a1', 'b3', 'c2'
            // or '1a', '2b', '3c', ...
            char x = str.charAt(1);
            char y = str.charAt(0);
            if (x >= 'a' && x <= 'z') {
                char tmp = x;
                x = y;
                y = tmp;
            }
            if (x >= '1' && x <= '3' && y >= 'a' && y <= 'c') {
                if (board.get(x - '1', y - 'a') != 0)
                    continue;
                return new Move(x - '1', y - 'a', curPlayer);
            }
        }
    }

    @Override
    protected void showResult() {
        int v = evaluate();
        if (v > 0) {
            System.out.printf("[%c] won.\n", PIECES[Player.PLAYER_1.getId()]);
        } else if (v < 0) {
            System.out.printf("[%c] won.\n", PIECES[Player.PLAYER_2.getId()]);
        } else {
            System.out.printf("Draw.\n");
        }
    }

}
