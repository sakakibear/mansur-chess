package game.othello;

import static game.Constants.PLAYER_1;
import static game.Constants.PLAYER_2;
import static game.othello.Constants.BOARD_SIZE;
import static game.othello.Constants.DARK;
import static game.othello.Constants.EMPTY;
import static game.othello.Constants.LIGHT;

import java.util.ArrayList;
import java.util.List;

/**
 * Game status of Othello game.
 */
public class GameStatus {
	private boolean isGameOver;

	private int winnerPlayer;

	private int darkCnt;

	private int lightCnt;

	public GameStatus(boolean isGameOver, int darkCnt, int lightCnt) {
		this.isGameOver = isGameOver;
		if (isGameOver) {
			if (darkCnt > lightCnt)
				this.winnerPlayer = PLAYER_1;
			else if (darkCnt < lightCnt)
				this.winnerPlayer = PLAYER_2;
			else
				this.winnerPlayer = 0;
		} else {
			this.winnerPlayer = 0;
		}
		this.darkCnt = darkCnt;
		this.lightCnt = lightCnt;
	}

	public boolean getIsGameOver() {
		return isGameOver;
	}

	public int getWinnerPlayer() {
		return winnerPlayer;
	}

	public int getDarkCnt() {
		return darkCnt;
	}

	public int getLightCnt() {
		return lightCnt;
	}
}