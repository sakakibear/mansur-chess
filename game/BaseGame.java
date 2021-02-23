package game;

import static game.Constants.DEFAULT_SEARCH_DEPTH;
import static game.Constants.PLAYER_1;
import static game.Constants.PLAYER_2;
import static game.Constants.VALUE_LOWER_BOUND;
import static game.Constants.VALUE_UPPER_BOUND;

import java.util.List;

/**
 * Abstract base class of a two-player game.
 */
public abstract class BaseGame {

    protected BaseBoard board;
    protected BaseEvaluator evaluator;
    protected int depth;
    protected boolean[] isHumanPlayer;

    public void init(String[] args) {
        depth = DEFAULT_SEARCH_DEPTH;
        isHumanPlayer = new boolean[PLAYER_2 + 1];
        isHumanPlayer[PLAYER_1] = true;
        isHumanPlayer[PLAYER_2] = false;
        // Command line option
        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-depth")) {
                    // Depth of game tree searching
                    depth = Integer.parseInt(args[++i]);
                } else if (args[i].equals("-md")) {
                    // Human player moves defensive
                    isHumanPlayer[PLAYER_1] = false;
                    isHumanPlayer[PLAYER_2] = true;
                }
            }
        } catch (Exception e) {
            System.err.println("Invalid option. See README.md.");
            return;
        }
    }

    abstract protected boolean isGameOver(int player);

    abstract protected List<? extends BaseMove> getValidMoves(int player);

    abstract protected void move(BaseMove move);

    abstract protected BaseMove getPlayerMove(int player);

    abstract protected void showResult();

    public void run(String[] args) {
        init(args);
        int curPlayer = PLAYER_1;
        while (true) {
            System.out.println(board);
            if (isGameOver(curPlayer))
                break;
            if (isHumanPlayer(curPlayer)) {
                move(getPlayerMove(curPlayer));
            } else {
                Node<BaseMove> root = makeTree(curPlayer, null, depth);
                SearchResult ab = alphabeta(root, depth, VALUE_LOWER_BOUND, VALUE_UPPER_BOUND);
                BaseMove move = root.getChildren().get(ab.idx).getMove();
                System.out.println(move);
                move(move);
            }
            // Switch current player
            curPlayer = curPlayer == PLAYER_1 ? PLAYER_2 : PLAYER_1;
        }
        showResult();
    }

    protected boolean isHumanPlayer(int player) {
        return isHumanPlayer[player];
    }

    protected <T extends BaseMove> Node<T> makeTree(int curPlayer, T move, int depth) {
        Node<T> root = new Node<T>();
        List<? extends BaseMove> moves = getValidMoves(curPlayer);
        if (depth == 0 || moves.size() == 0) {
            // Evaluation is based on player 1
            int v = evaluate() * (curPlayer == PLAYER_1 ? 1 : -1);
            root.setVal(v);
        }
        root.setMove(move);
        if (depth > 0) {
            for (BaseMove m : moves) {
                BaseBoard backupBoard = board.clone();
                move(m);
                @SuppressWarnings("unchecked")
                T tMove = (T) m;
                // Switch player
                root.addChild(makeTree(curPlayer == PLAYER_1 ? PLAYER_2 : PLAYER_1, tMove, depth - 1));
                board = backupBoard;
            }
        }
        return root;
    }

    protected int evaluate() {
        return evaluator.evaluate(board);
    }

    protected <T extends BaseMove> SearchResult alphabeta(Node<T> node, int depth, int alpha, int beta) {
        if (depth == 0 || node.getChildren().size() == 0)
            return new SearchResult(node.getVal(), -1);
        int selectedIdx = -1;
        for (int i = 0; i < node.getChildren().size(); i++) {
            Node<T> child = node.getChildren().get(i);
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
