package game.tictactoe;

public class Move {
    private int x;
    private int y;
    private int player; // 1 or 2

    public Move(int x, int y, int player) {
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

    public int getPlayer() {
        return player;
    }

    // XXX
    public String toString() {
        String str = "";
        str += (char)('a' + y);
        str += (x + 1);
//        str += " (" + player + ")";
        return str;
    }
}
