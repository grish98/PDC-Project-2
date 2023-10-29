/*
 * Represents a listener for game events.
 * gameController  implement this interface can react to ending game events.
 * 
 * @author Grisham Balloo 20099072
 */
package minesweepertest1;

/**
 *
 * @author Grisham Balloo 20099072
 */
public interface GameEventListener {
    void onGameEnd(String message);
}
