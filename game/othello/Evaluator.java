package game.othello;

import static game.othello.Constants.BOARD_SIZE;
import static game.othello.Constants.CORNER_POINT;
import static game.othello.Constants.DARK;
import static game.othello.Constants.EMPTY;
import static game.othello.Constants.LIGHT;
import static game.othello.Constants.UNSTABLE_X_DEDUCTION;
import static game.othello.Constants.VALUE_LOSE;
import static game.othello.Constants.VALUE_WIN;

import game.BaseBoard;
import game.BaseEvaluator;

public class Evaluator extends BaseEvaluator {

    protected Rule rule = Rule.getInstance();

    @Override
    public int evaluate(BaseBoard board) {
        // TODO Could be solved using generic
        Board b = (Board) board;
        boolean isGameOver = rule.isGameOver(b);
        int darkCnt = 0, lightCnt = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                int disc = b.get(i, j);
                if (disc == DARK)
                    darkCnt++;
                else if (disc == LIGHT)
                    lightCnt++;
            }
        }
        if (isGameOver) {
            if (darkCnt > lightCnt)
                return VALUE_WIN;
            else if (darkCnt == lightCnt)
                return 0;
            else
                return VALUE_LOSE;
        }
        return darkCnt - lightCnt + getCornerAdjustment(b);
    }

    protected int getCornerAdjustment(Board board) {
        int res = 0;
        res += getCornerValue(board.get(0, 0), board.get(1, 1));
        res += getCornerValue(board.get(0, 7), board.get(1, 6));
        res += getCornerValue(board.get(7, 0), board.get(6, 1));
        res += getCornerValue(board.get(7, 7), board.get(6, 6));
        return res;
    }

    protected int getCornerValue(int corner, int x) {
        if (corner == EMPTY) {
            if (x == DARK)
                return -UNSTABLE_X_DEDUCTION;
            else if (x == LIGHT)
                return UNSTABLE_X_DEDUCTION;
            return 0;
        }
        if (corner == DARK)
            return CORNER_POINT;
        else
            return -CORNER_POINT;
    }
}
