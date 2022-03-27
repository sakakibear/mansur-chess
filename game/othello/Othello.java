package game.othello;

import static game.othello.Constants.BOARD_SIZE;
import static game.othello.Constants.DARK;
import static game.othello.Constants.DISCS;
import static game.othello.Constants.LIGHT;

import java.util.List;
import java.util.Scanner;

import game.BaseGame;

public class Othello extends BaseGame<Board, Move> {

    protected Rule rule = Rule.getInstance();

    // Scanner to get user input
    protected Scanner scanner;

    public static void main(String[] args) {
        Othello game = new Othello();
        game.run(args);
    }

    @Override
    public void init(String[] args) {
        super.init(args);
        board = new Board();
        board.init();
        evaluator = new Evaluator();
        scanner = new Scanner(System.in);
    }

    @Override
    protected boolean isGameOver(int player) {
        return rule.isGameOver(board);
    }

    @Override
    protected List<Move> getValidMoves(int player) {
        return rule.getMoves(board, player);
    }

    @Override
    protected void move(Move move) {
        rule.takeMove(board, move);
    }

    @Override
    protected Move getPlayerMove(int player) {
        List<Move> moves = getValidMoves(player);
        if (moves.size() < 1 || moves.size() == 1 && moves.get(0).isPass()) {
            System.out.print("PASS");
            scanner.nextLine();
            return new Move(player);
        }
        while (true) {
            System.out.printf("[%c] > ", DISCS[player]);
            String str = scanner.nextLine();
            str = str.trim();
            if (str.equalsIgnoreCase("help")) {
                // TODO Show possible moves to player
                continue;
            }
            // User input should be like 'a1', 'b3', 'c2'
            // or '1a', '2b', '3c', ...
            if (str.length() != 2)
                continue;
            char cx = str.charAt(1);
            char cy = str.charAt(0);
            if (cx >= 'a' && cx <= 'z') {
                char tmp = cx;
                cx = cy;
                cy = tmp;
            }
            int x = cx - '1', y = cy - 'a';
            if (rule.isValidMove(board, player, x, y))
                return new Move(x, y, player);
        }
    }

    @Override
    protected void showResult() {
        // ● 18:46 ○
        // ● 60: 1 ○
        // ● 32:32 ○
        int cntDark = 0, cntLight = 0;
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                if (board.get(i, j) == DARK)
                    cntDark++;
                else if (board.get(i, j) == LIGHT)
                    cntLight++;
        System.out.printf("%c %2d:%2d %c", DISCS[DARK], cntDark, cntLight, DISCS[LIGHT]);
        System.out.println();
    }

}
