package game;

/**
 * Evaluates the score of a board.
 */
public abstract class BaseEvaluator {
    /**
     * Evaluate current state of game based on player1 and return the value.
     * Note: this function has the most influence on AI performance.
     * 
     * @param board
     * @return value
     */
    public abstract int evaluate(BaseBoard board);
}
