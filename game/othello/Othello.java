package game.othello;

import static game.Constants.PLAYER_1;
import static game.Constants.PLAYER_2;
import static game.othello.Constants.BOARD_SIZE;
import static game.othello.Constants.DARK;
import static game.othello.Constants.EMPTY;
import static game.othello.Constants.LIGHT;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import game.BaseGame;
import game.BaseMove;

public class Othello extends BaseGame {

    // Scanner to get user input
    protected Scanner scanner;

    // 8 directions on the board
    protected static int[][] dirs = { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 },
            { 1, 1 }, };

    // XXX for test
    @Override
    public void run(String[] args) {
        init(args);
        Random rand = new Random();
        System.out.println(board);
        int curPlayer = PLAYER_1;
        while (!isGameOver(curPlayer)) {
            List<Move> moves = getValidMoves(curPlayer);

            System.out.println("\nPossible moves:\n");
            for (Move m : moves)
                System.out.println(m);

            System.out.println();
            int idx = rand.nextInt(moves.size());
            Move m = moves.get(idx);
            System.out.println(m);
            move(m);

            System.out.println(board);
            curPlayer = curPlayer == PLAYER_1 ? PLAYER_2 : PLAYER_1;
        }
    }

    public static void main(String[] args) {
        Othello game = new Othello();
        game.run(args);
    }

    @Override
    public void init(String[] args) {
        super.init(args);
        Board othelloBoard = new Board();
        // TODO Use generic.
        board = othelloBoard;
        othelloBoard.init();
        // TODO evaluator
        scanner = new Scanner(System.in);
    }

    @Override
    protected boolean isGameOver(int player) {
        List<Move> moves1 = getValidMoves(PLAYER_1);
        if (moves1.size() >= 1 && !moves1.get(0).isPass())
            return false;
        List<Move> moves2 = getValidMoves(PLAYER_2);
        if (moves2.size() >= 1 && !moves2.get(0).isPass())
            return false;
        return true;
    }

    @Override
    protected List<Move> getValidMoves(int player) {
        Board b = (Board) board;
        List<Move> result = new ArrayList<Move>();
        for (int i = 0; i < BOARD_SIZE; i++)
            for (int j = 0; j < BOARD_SIZE; j++)
                if (isValidMove(b, player, i, j))
                    result.add(new Move(i, j, player));
        // Add a pass move if no moves could be done
        if (result.size() == 0)
            result.add(new Move(player));
        return result;
    }

    protected boolean isValidMove(Board b, int player, int x, int y) {
        if (b.get(x, y) != EMPTY)
            return false;
        int self = player == PLAYER_1 ? DARK : LIGHT;
        int oppo = player == PLAYER_1 ? LIGHT : DARK;
        for (int[] dir : dirs) {
            int i = x + dir[0], j = y + dir[1];
            boolean hasOppo = false;
            while (i >= 0 && i < BOARD_SIZE && j >= 0 && j < BOARD_SIZE && b.get(i, j) == oppo) {
                hasOppo = true;
                i += dir[0];
                j += dir[1];
            }
            if (hasOppo && i >= 0 && i < BOARD_SIZE && j >= 0 && j < BOARD_SIZE && b.get(i, j) == self)
                return true;
        }
        return false;
    }

    @Override
    protected void move(BaseMove move) {
        // TODO Use generic
        Move m = (Move) move;
        if (m.isPass())
            return;
        Board b = (Board) board;
        int player = m.getPlayer();
        int x = m.getX();
        int y = m.getY();
        int self = player == PLAYER_1 ? DARK : LIGHT;
        int oppo = player == PLAYER_1 ? LIGHT : DARK;
        b.set(x, y, self);
        for (int[] dir : dirs) {
            int i = x + dir[0], j = y + dir[1];
            int cnt = 0;
            while (i >= 0 && i < BOARD_SIZE && j >= 0 && j < BOARD_SIZE && b.get(i, j) == oppo) {
                cnt++;
                i += dir[0];
                j += dir[1];
            }
            if (i >= 0 && i < BOARD_SIZE && j >= 0 && j < BOARD_SIZE && b.get(i, j) == self) {
                int ii = x + dir[0], jj = y + dir[1];
                for (int k = 0; k < cnt; k++) {
                    b.set(ii, jj, self);
                    ii += dir[0];
                    jj += dir[1];
                }
            }
        }
    }

    @Override
    protected Move getPlayerMove(int player) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void showResult() {
        // TODO Auto-generated method stub

    }

}
