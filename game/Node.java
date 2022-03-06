package game;

import java.util.ArrayList;
import java.util.List;

/**
 * Node of a search tree. Contains evaluation value, the move taken and possible
 * next moves.
 */
public class Node<T extends BaseMove> {
    // Evaluation value
    protected int val;
    // Current move, which is taken (can be null)
    protected T move;
    // Possible next moves
    protected List<Node<T>> children;

    public Node() {
        children = new ArrayList<Node<T>>();
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

    public T getMove() {
        return move;
    }

    public void setMove(T move) {
        this.move = move;
    }

    public List<Node<T>> getChildren() {
        return children;
    }

    public void addChild(Node<T> node) {
        children.add(node);
    }
}
