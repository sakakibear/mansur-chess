package game.tictactoe;

import java.util.ArrayList;
import java.util.List;

public class Node {

	private int val;
	private Move move;
	private List<Node> children;

	public Node() {
		children = new ArrayList<Node>();
	}

	public Node(int val) {
		this();
		this.setVal(val);
	}

	public int getVal() {
		return val;
	}

	public void setVal(int val) {
		this.val = val;
	}

	public Move getMove() {
		return move;
	}

	public void setMove(Move move) {
		this.move = move;
	}

	public List<Node> getChildren() {
		return children;
	}

	public void addChild(Node node) {
		children.add(node);
	}
}
