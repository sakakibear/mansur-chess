package game.tictactoe;

import static game.tictactoe.Constants.BOARD_SIZE;
import static game.tictactoe.Constants.DEFAULT_SEARCH_DEPTH;
import static game.tictactoe.Constants.PIECES;
import static game.tictactoe.Constants.PLAYER_AI;
import static game.tictactoe.Constants.PLAYER_HUMAN;
import static game.tictactoe.Constants.VALUE_LOSE;
import static game.tictactoe.Constants.VALUE_LOWER_BOUND;
import static game.tictactoe.Constants.VALUE_UPPER_BOUND;
import static game.tictactoe.Constants.VALUE_WIN;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TicTacToe {

    // Chess board
    private int[][] board;
    // Evaluator
    private Evaluator evaluator;
    // Scanner to get user input
    private Scanner scanner;

    public TicTacToe() {
        board = new int[BOARD_SIZE][BOARD_SIZE];
        evaluator = new Evaluator();
        scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();
        int depth = DEFAULT_SEARCH_DEPTH;
        int curPlayer = PLAYER_HUMAN;

        // Command line option
        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-depth")) {
                    // Depth of game tree searching
                    depth = Integer.parseInt(args[++i]);
                } else if (args[i].equals("-md")) {
                    // Move defensive
                    curPlayer = PLAYER_AI;
                }
            }
        } catch (Exception e) {
            System.err.println("Invalid option. See README.md.");
            return;
        }

        while (true) {
            game.printBoard();
            if (game.isGameOver(curPlayer))
                break;
            if (curPlayer == PLAYER_HUMAN) {
                // Human player
                game.move(game.getPlayerMove());
            } else {
                // PC
                Node root = game.makeTree(curPlayer, null, depth);
                SearchResult ab = game.alphabeta(root, depth, VALUE_LOWER_BOUND, VALUE_UPPER_BOUND);
                Move move = root.getChildren().get(ab.idx).getMove();
                System.out.printf("[%c] > %s\n", PIECES[PLAYER_AI], move);
                game.move(move);
            }
            curPlayer = curPlayer == PLAYER_HUMAN ? PLAYER_AI : PLAYER_HUMAN;
        }

        int v = game.evaluate();
        if (v > 0) {
            System.out.println("Player win");
        } else if (v < 0) {
            System.out.println("PC win");
        } else {
            System.out.println("Draw");
        }
    }

    private void printBoard() {
        System.out.println("\n  a b c");
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.printf("%d ", i + 1);
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.printf("%c ", PIECES[board[i][j]]);
            }
            System.out.printf("%d", i + 1);
            System.out.println();
        }
        System.out.println("  a b c\n");
    }

    private Move getPlayerMove() {
        while (true) {
            System.out.printf("[%c] > ", PIECES[PLAYER_HUMAN]);
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
                if (board[x - '1'][y - 'a'] != 0)
                    continue;
                return new Move(x - '1', y - 'a', 1);
            }
        }
    }

    private boolean isGameOver(int curPlayer) {
        List<Move> moves = getValidMoves(curPlayer);
        if (moves.size() == 0)
            return true;
        int value = evaluate();
        if (value == VALUE_WIN || value == VALUE_LOSE)
            return true;
        return false;
    }

    private int evaluate() {
        return evaluator.evaluate(board);
    }

    /**
     * Generate all possible next moves.
     * 
     * @param player
     * @return list of moves
     */
    private List<Move> getValidMoves(int player) {
        List<Move> result = new ArrayList<Move>();
        int value = evaluate();
        if (value == VALUE_WIN || value == VALUE_LOSE)
            return result;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == 0)
                    result.add(new Move(i, j, player));
            }
        }
        return result;
    }

    private void move(Move move) {
        board[move.getX()][move.getY()] = move.getPlayer();
    }

    private void unmove(Move move) {
        board[move.getX()][move.getY()] = 0;
    }

    /**
     * Build the search tree. Root is current state of game.
     * 
     * @param curPlayer
     * @param move
     * @param depth
     * @return search tree
     */
    private Node makeTree(int curPlayer, Move move, int depth) {
        Node root = new Node();
        List<Move> moves = getValidMoves(curPlayer);
        if (depth == 0 || moves.size() == 0) {
            int v = evaluate() * (curPlayer == PLAYER_HUMAN ? 1 : -1);
            root.setVal(v);
        }
        root.setMove(move);
        if (depth > 0) {
            for (Move m : moves) {
                move(m);
                root.addChild(makeTree(curPlayer == PLAYER_HUMAN ? PLAYER_AI : PLAYER_HUMAN, m, depth - 1));
                unmove(m);
            }
        }
        return root;
    }

    /**
     * Alpha-beta pruning search of a game tree.
     * 
     * @param node
     * @param depth
     * @param alpha
     * @param beta
     * @return search result
     */
    private SearchResult alphabeta(Node node, int depth, int alpha, int beta) {
        if (depth == 0 || node.getChildren().size() == 0)
            return new SearchResult(node.getVal(), -1);
        int selectedIdx = -1;
        for (int i = 0; i < node.getChildren().size(); i++) {
            Node child = node.getChildren().get(i);
            int value = -1 * alphabeta(child, depth - 1, -1 * beta, -1 * alpha).val;
            if (value > alpha) {
                alpha = value;
                selectedIdx = i;
            }
            if (alpha > beta)
                return new SearchResult(alpha, selectedIdx);
        }
        return new SearchResult(alpha, selectedIdx);
    }
}
