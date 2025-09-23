package game;

import static game.Constants.DEFAULT_SEARCH_DEPTH;
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
    protected int numPlayers;
    protected boolean[] isHumanPlayer;

    public void init(String[] args) {
        depth = DEFAULT_SEARCH_DEPTH;
        numPlayers = 2;
        isHumanPlayer = new boolean[numPlayers + 1];
        isHumanPlayer[Player.PLAYER_1.getId()] = true;
        isHumanPlayer[Player.PLAYER_2.getId()] = false;
        // Command line option
        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-depth")) {
                    // Depth of game tree searching
                    depth = Integer.parseInt(args[++i]);
                } else if (args[i].equals("-md")) {
                    // Human player moves defensive
                    isHumanPlayer[Player.PLAYER_1.getId()] = false;
                    isHumanPlayer[Player.PLAYER_2.getId()] = true;
                }
            }
        } catch (Exception e) {
            System.err.println("Invalid option. See README.md.");
            return;
        }
    }

    abstract protected boolean isGameOver(Player player);

    abstract protected List<M> getValidMoves(Player player);

    abstract protected void move(M move);

    abstract protected M getUserPlayerMove(Player player);

    abstract protected void showResult();

    public void run(String[] args) {
        init(args);
        Player curPlayer = Player.PLAYER_1;
        while (true) {
            System.out.println(board);
            if (isGameOver(curPlayer))
                break;

            move(getPlayerMove(curPlayer));
            curPlayer = getNextPlayer(curPlayer);
        }
        showResult();
    }

    protected Player getNextPlayer(Player curPlayer) {
        return curPlayer == Player.PLAYER_1
            ? Player.PLAYER_2
            : Player.PLAYER_1;
    }

    protected M getPlayerMove(Player curPlayer) {
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

    protected boolean isHumanPlayer(Player player) {
        return isHumanPlayer[player.getId()];
    }

    protected Node<M> makeTree(Player curPlayer, M move, int depth) {
        Node<M> root = new Node<M>();
        List<M> moves = getValidMoves(curPlayer);
        root.setMove(move);
        if (depth == 0 || moves.size() == 0) {
            // Evaluation is based on player 1
            int v = evaluate() * (curPlayer == Player.PLAYER_1 ? 1 : -1);
            root.setVal(v);
        } else {
            expandNode(root, curPlayer, depth, moves);
        }
        return root;
    }

    protected void expandNode(Node<M> parent, Player curPlayer, int depth, List<M> moves) {
        for (M m : moves) {
            @SuppressWarnings("unchecked")
            // Save the board
            B backupBoard = (B) board.clone();
            // Take move
            move(m);
            // Switch player in next depth
            Node<M> child = makeTree(getNextPlayer(curPlayer), m, depth - 1);
            parent.addChild(child);
            // Restore the board
            board = backupBoard;
        }
    }

    @SuppressWarnings("unchecked")
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
