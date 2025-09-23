package game.tictactoe;

import static game.tictactoe.Constants.PIECES;

import game.BaseMove;
import game.Player;

/**
 * A player's move in the chess. In Tic-Tac-Toe, it contains of the position (x,
 * y) and the player who made this move.
 */
public class Move extends BaseMove {

    private int x;
    private int y;
    private Player player;

    public Move(int x, int y, Player player) {
        this.x = x;
        this.y = y;
        this.player = player;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return String.format("[%c] > %c%d", PIECES[player.getId()], (char) ('a' + y), (x + 1));
    }
}
