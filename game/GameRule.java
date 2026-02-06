package game;

import java.util.List;

public interface GameRule<M, P, B> {

    boolean isLegalMove(B board, M move);

    List<M> getLegalMoves(B board, P player);

    void makeMove(B board, M move);

    boolean isGameOver(B board);
}
