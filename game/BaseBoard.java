package game;

/**
 * Board of a game. Data structure should be defined in subclasses.
 */
public abstract class BaseBoard implements Cloneable {
    @Override
    public abstract BaseBoard clone();

    @Override
    public abstract String toString();
}
