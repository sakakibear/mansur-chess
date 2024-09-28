package game.othello;

import static game.othello.Constants.DISCS;

import game.BaseMove;

/**
 * A player's move in an Othello game.
 */
public class Move extends BaseMove {

    private int x;
    private int y;
    private int player;
    private boolean isPass;

    public Move(int x, int y, int player) {
        this.x = x;
        this.y = y;
        this.player = player;
        this.isPass = false;
    }

    // Pass move
    public Move(int player) {
        this.x = -1;
        this.y = -1;
        this.player = player;
        this.isPass = true;
    }

    @Override
    public String toString() {
        return String.format("[%c] > %s", DISCS[player], this.toSimpleString());
    }

    public String toSimpleString() {
        if (isPass)
            return String.format("PASS");
        return String.format("%c%d", (char) ('a' + y), x + 1);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPlayer() {
        return player;
    }

    public boolean isPass() {
        return isPass;
    }
}
