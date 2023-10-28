/**
 * Represents the game board containing a grid of cells.
 * Handles operations such as initializing the board, placing mines, 
 * revealing cells, and calculating adjacent mine counts.
 */
package minesweepertest1;

import java.util.StringTokenizer;

/**
 *
 * @author Grisham Balloo 20099072
 */
public class Cell {
    private boolean isMine;
    private boolean isRevealed;
    private boolean isFlagged;
    private int neighboringMines;
    private int x, y;

    public Cell() {
        this.isMine = false;
        this.isRevealed = false;
        this.isFlagged = false;
        this.neighboringMines = 0;
        this.x =0;
        this.y = 0;
    }

    Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        
        
        
       this. isMine = mine;
        
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void reveal() {
        this.isRevealed = true;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void toggleFlag() {
        this.isFlagged = !this.isFlagged;
    }

    public int getNeighboringMines() {
        return neighboringMines;
    }

    public void setNeighboringMines(int neighboringMines) {
        this.neighboringMines = neighboringMines;
    }
    
    // Setter for revealed attribute
    public void setRevealed(boolean revealed) {
        
        this.isRevealed = revealed;
        
    }

    // Setter for flagged attribute
    public void setFlagged(boolean flagged) {
        this.isFlagged = flagged;
    }

    @Override
    public String toString() {
        if (isFlagged) {
            System.out.print("F");
        } else if (!isRevealed) {
            System.out.print("-");
        } else if (isMine) {
            System.out.print("*");
        } else if (neighboringMines > 0) {
            System.out.print(neighboringMines);
        } else {
            System.out.print(" ");
        }
    
      //  System.out.println("Debug: Displaying cell with state: " + (isMine ? "Mine" : "Non-Mine"));
        return String.valueOf(neighboringMines);
    }
    public String display() {
        String str = null;
        if (isFlagged) {
            str = "F";
        } else if (!isRevealed) {
             str = "-";
        } else if (isMine) {
             str = "M";
        } else if (neighboringMines >= 0) {
            System.out.print(neighboringMines);
        } else {
             str=  " ";
        }
        return str;
    }
    public static Cell fromString(String data) {
    StringTokenizer tokenizer = new StringTokenizer(data, ";");

    if (tokenizer.countTokens() != 3) {
        throw new IllegalArgumentException("Invalid cell data format.");
    }

    boolean isMine = "1".equals(tokenizer.nextToken());
    boolean isRevealed = "1".equals(tokenizer.nextToken());
    boolean isFlagged = "1".equals(tokenizer.nextToken());

    Cell cell = new Cell();
    cell.setMine(isMine);
    cell.setRevealed(isRevealed);
    cell.setFlagged(isFlagged);

    return cell;
}

    
}
