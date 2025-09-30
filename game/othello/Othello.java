package game.othello;

import static game.othello.Constants.BOARD_SIZE;
import static game.othello.Constants.DARK;
import static game.othello.Constants.DISCS;
import static game.othello.Constants.LIGHT;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import game.BaseGame;
import game.Player;

public class Othello extends BaseGame<Board, Move> {

    protected Rule rule = Rule.getInstance();

    // Scanner to get user input
    protected Scanner scanner;

    public static void main(String[] args) {
        Othello game = new Othello();
        game.run(args);
    }

    @Override
    public void init() {
        super.init();
        board = new Board();
        board.init();
        evaluator = new Evaluator();
        scanner = new Scanner(System.in);
    }

    @Override
    protected boolean isGameOver(Player player) {
        return rule.isGameOver(board);
    }

    @Override
    protected List<Move> getValidMoves(Player player) {
        return rule.getMoves(board, player);
    }

    @Override
    protected void move(Move move) {
        rule.takeMove(board, move);
    }

    @Override
    protected Move getUserPlayerMove(Player player) {
        List<Move> moves = getValidMoves(player);
        if (moves.size() < 1 || moves.size() == 1 && moves.get(0).isPass()) {
            System.out.print("PASS");
            scanner.nextLine();
            return new Move(player);
        }
        while (true) {
            System.out.printf("[%c] > ", DISCS[player.getId()]);
            String str = scanner.nextLine();
            str = str.trim();
            if (str.equalsIgnoreCase("help")) {
                Collections.sort(moves, new Comparator<Move>() {
                    @Override
                    public int compare(Move m1, Move m2) {
                        if (m1.getY() == m2.getY())
                            return m1.getX() - m2.getX();
                        return m1.getY() - m2.getY();
                    }
                });
                for (int i = 0; i < moves.size(); i++) {
                    System.out.printf(
                        "%s%s",
                        i % 4 == 0 ? (i == 0 ? "" : "\n") : "\t",
                        moves.get(i).toSimpleString()
                    );
                }
                System.out.println();
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
