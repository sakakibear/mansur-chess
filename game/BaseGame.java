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
public abstract class BaseGame<B extends BaseBoard, M extends BaseMove> {

    protected B board;
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

    abstract protected List<M> getValidMoves(int player);

    abstract protected void move(M move);

    abstract protected M getUserPlayerMove(int player);

    abstract protected void showResult();

    public void run(String[] args) {
        init(args);
        int curPlayer = PLAYER_1;
        while (true) {
            System.out.println(board);
            if (isGameOver(curPlayer))
                break;

            move(getPlayerMove(curPlayer));

            // Switch current player
            curPlayer = curPlayer == PLAYER_1 ? PLAYER_2 : PLAYER_1;
        }
        showResult();
    }

    protected M getPlayerMove(int curPlayer) {
        if (isHumanPlayer(curPlayer)) {
            // Human (user)
            return getUserPlayerMove(curPlayer);
        } else {
            // AI
            Node<M> root = makeTree(curPlayer, null, depth);
            SearchResult ab = alphabeta(root, depth, VALUE_LOWER_BOUND, VALUE_UPPER_BOUND);
            M move = root.getChildren().get(ab.getIdx()).getMove();

            System.out.println(move);
            return move;
        }
    }

    protected boolean isHumanPlayer(int player) {
        return isHumanPlayer[player];
    }

    protected Node<M> makeTree(int curPlayer, M move, int depth) {
        Node<M> root = new Node<M>();
        List<M> moves = getValidMoves(curPlayer);
        if (depth == 0 || moves.size() == 0) {
            // Evaluation is based on player 1
            int v = evaluate() * (curPlayer == PLAYER_1 ? 1 : -1);
            root.setVal(v);
        }
        root.setMove(move);
        if (depth > 0) {
            for (M m : moves) {
                @SuppressWarnings("unchecked")
                B backupBoard = (B) board.clone();
                move(m);
                // Switch player
                root.addChild(makeTree(curPlayer == PLAYER_1 ? PLAYER_2 : PLAYER_1, m, depth - 1));
                board = backupBoard;
            }
        }
        return root;
    }

    protected int evaluate() {
        return evaluator.evaluate(board);
    }

    protected SearchResult alphabeta(Node<M> node, int depth, int alpha, int beta) {
        if (depth == 0 || node.getChildren().size() == 0)
            return new SearchResult(node.getVal(), -1);
        int selectedIdx = -1;
        for (int i = 0; i < node.getChildren().size(); i++) {
            Node<M> child = node.getChildren().get(i);
            int value = -1 * alphabeta(child, depth - 1, -1 * beta, -1 * alpha).getVal();
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
