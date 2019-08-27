package game.tictactoe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class TicTacToe {

    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();
        Random rnd = new Random();
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
                // Player
                game.move(game.getPlayerMove());
            } else {
                // PC
                Node root = game.makeTree(curPlayer, null, depth);
                AB ab = game.alphabeta(root, depth, -1001, 1001);
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

    private Move getPlayerMove() {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.print("[O] > ");
            String str = scan.nextLine();
            str = str.trim();
            if (str.length() != 2)
                continue;
            char x = str.charAt(1);
            char y = str.charAt(0);
            if (x >= '1' && x <= '3' && y >= 'a' && y <= 'c') {
                if (board[x - '1'][y - 'a'] != 0)
                    continue;
                return new Move(x - '1', y - 'a', 1);
            }
        }
    }

    private void traverse(Node root) {
        System.out.printf("%s %d\n",
        root.getMove(), root.getVal());
        for (Node n : root.getChildren()) {
            traverse(n);
        }
    }

    private int[][] board;
    private int size;

    public TicTacToe() {
        size = 3;
        board = new int[size][size];
    }

    private int evaluate() {
        int value = 0;
        for (int i = 0; i < size; i++) {
            int[] line = new int[size];
            for (int j = 0; j < size; j++) {
                line[j] = board[i][j];
            }
            int v = getValue(line);
            if (v == 1000 || v == -1000)
                return v;
            value += v;
        }
        for (int j = 0; j < size; j++) {
            int[] line = new int[size];
            for (int i = 0; i < size; i++) {
                line[i] = board[i][j];
            }
            int v = getValue(line);
            if (v == 1000 || v == -1000)
                return v;
            value += v;
        }
        
        int[] line = new int[size];
        for (int i = 0; i < size; i++) {
            line[i] = board[i][i];
        }
        int v = getValue(line);
        if (v == 1000 || v == -1000)
            return v;
        value += v;
        for (int i = 0; i < size; i++) {
            line[i] = board[i][size - i - 1];
        }
        v = getValue(line);
        if (v == 1000 || v == -1000)
            return v;
        value += v;
        return value;
    }

    private int getValue(int[] line) {
        int p1 = 0, p2 = 0;
        for (int n : line) {
            if (n == 1)
                p1++;
            if (n == 2)
                p2++;
        }
        if (p1 > 0 && p2 > 0)
            return 0;
        if (p1 == 3)
            return 1000;
        if (p1 == 2)
            return 10;
        if (p1 == 1)
            return 1;
        if (p2 == 3)
            return -1000;
        if (p2 == 2)
            return -10;
        if (p2 == 1)
            return -1;
        return 0;
    }

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

    // SearchTree
    class AB {
        int val;
        int idx;

        public AB() {
            val = idx = 0;
        }

        public AB(int val, int idx) {
            this.val = val;
            this.idx = idx;
        }
    }

    private AB alphabeta(Node node, int depth, int alpha, int beta) {
        if (depth == 0 || node.getChildren().size() == 0)
            return new AB(node.getVal(), -1);
        int selectedIdx = -1;
        for (int i = 0; i < node.getChildren().size(); i++) {
            Node child = node.getChildren().get(i);
            int value = -1 * alphabeta(child, depth - 1, -1 * beta, -1 * alpha).val;
            if (value > alpha) {
                alpha = value;
                selectedIdx = i;
            }
            if (alpha > beta)
                return new AB(alpha, selectedIdx);
        }
        return new AB(alpha, selectedIdx);
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
}
