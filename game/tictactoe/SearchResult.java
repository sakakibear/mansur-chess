package game.tictactoe;

/**
 * Search result of game tree. Contains the value of the selected move and it's
 * index in the parent.
 */
public class SearchResult {
    int val;
    int idx;

    public SearchResult() {
        val = idx = 0;
    }

    public SearchResult(int val, int idx) {
        this.val = val;
        this.idx = idx;
    }
}
