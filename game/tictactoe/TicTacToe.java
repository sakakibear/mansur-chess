package game.tictactoe;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TicTacToe {

    // Chess board
    private int[][] board;
    private int size;
    // Scanner to get user input
    private Scanner scanner;

    public TicTacToe() {
        size = 3;
        board = new int[size][size];
        scanner = new Scanner(System.in);
    }

    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();
        int depth = 5;
        int curPlayer = 1;

        try {
            depth = Integer.parseInt(args[0]);
            if (args.length > 1)
                curPlayer = 2;
        } catch (Exception e) {

        }

        while (true) {
            game.printBoard();
            if (game.isGameOver(curPlayer))
                break;
            if (curPlayer == 1) {
                // Human player
                game.move(game.getPlayerMove());
            } else {
                // PC
                Node root = game.makeTree(curPlayer, null, depth);
                SearchResult ab = game.alphabeta(root, depth, -1001, 1001);
                Move move = root.getChildren().get(ab.idx).getMove();
                System.out.printf("[X] > %s\n", move);
                game.move(move);
            }
            curPlayer = curPlayer == 1 ? 2 : 1;
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
        for (int i = 0; i < size; i++) {
            System.out.printf("%d ", i + 1);
            for (int j = 0; j < size; j++) {
                System.out.printf("%s ", board[i][j] == 0 ? " " : board[i][j] == 1 ? "O" : "X");
            }
            System.out.printf("%d", i + 1);
            System.out.println();
        }
        System.out.println("  a b c\n");
    }

    private Move getPlayerMove() {
        while (true) {
            System.out.print("[O] > ");
            String str = scanner.nextLine();
            str = str.trim();
            if (str.length() != 2)
                continue;
            char x = str.charAt(1);
            char y = str.charAt(0);
            // User input should look like 'a1', 'b3', 'c2' ...
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
        if (value == 1000 || value == -1000)
            return true;
        return false;
    }

    /**
     * Evaluate current state of game and return the value. Note: this function
     * has the most influence on AI performance.
     * 
     * @return value
     */
    private int evaluate() {
        int value = 0;
        // check the rows
        for (int i = 0; i < size; i++) {
            int[] line = new int[size];
            for (int j = 0; j < size; j++) {
                line[j] = board[i][j];
            }
            int v = getValueOfLine(line);
            if (v == 1000 || v == -1000)
                return v;
            value += v;
        }
        // check the columns
        for (int j = 0; j < size; j++) {
            int[] line = new int[size];
            for (int i = 0; i < size; i++) {
                line[i] = board[i][j];
            }
            int v = getValueOfLine(line);
            if (v == 1000 || v == -1000)
                return v;
            value += v;
        }
        // check the diagonal lines
        int[] line = new int[size];
        for (int i = 0; i < size; i++) {
            line[i] = board[i][i];
        }
        int v = getValueOfLine(line);
        if (v == 1000 || v == -1000)
            return v;
        value += v;
        for (int i = 0; i < size; i++) {
            line[i] = board[i][size - i - 1];
        }
        v = getValueOfLine(line);
        if (v == 1000 || v == -1000)
            return v;
        value += v;
        return value;
    }

    /**
     * Get the evaluation result of a certain line of the board. Could be
     * horizontal, vertical or diagonal since the rules are the same.
     * 
     * @param line
     * @return value
     */
    private int getValueOfLine(int[] line) {
        int p1 = 0, p2 = 0;
        for (int n : line) {
            if (n == 1)
                p1++;
            if (n == 2)
                p2++;
        }
        if (p1 > 0 && p2 > 0)
            return 0;
        // Three in a line means winning
        if (p1 == 3)
            return 1000;
        if (p1 == 2)
            return 10;
        if (p1 == 1)
            return 1;
        // Three in a line of the other player means losing
        if (p2 == 3)
            return -1000;
        if (p2 == 2)
            return -10;
        if (p2 == 1)
            return -1;
        return 0;
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
        if (value == 1000 || value == -1000)
            return result;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
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
            int v = evaluate() * (curPlayer == 1 ? 1 : -1);
            root.setVal(v);
        }
        root.setMove(move);
        if (depth > 0) {
            for (Move m : moves) {
                move(m);
                root.addChild(makeTree(curPlayer == 1 ? 2 : 1, m, depth - 1));
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
