package game.tictactoe;

import static game.tictactoe.Constants.PIECES;

import java.util.Scanner;

import game.BaseGame;
import game.Player;

public class TicTacToe extends BaseGame<Board, Move, Rule> {

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
        rule = new Rule();
        evaluator = new Evaluator();
        scanner = new Scanner(System.in);
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
        Player winnerPlayer = rule.getWinner(board);
        if (winnerPlayer == null)
            System.out.printf("Draw.\n");
        else
            System.out.printf("[%c] won.\n", PIECES[winnerPlayer.getId()]);
    }

}
