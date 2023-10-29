/**
 * Represents the game board containing a grid of cells.
 * Handles operations such as initializing the board, placing mines, 
 * revealing cells, and calculating adjacent mine counts.
 */
package minesweepertest1;

/**
 *
 *  @author Grisham Balloo 20099072
 * Initializes a new board with given dimensions.
 * @param width Width of the board.
 * @param height Height of the board.
 */


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;


public class Board {
    // List to keep track of moves made on the board.
    private List<Move> moves = new ArrayList<>();
    // A 2D array of cells representing the game board.
    Cell[][] cells;
    
    private int width;         // Width of the board.
    private int height;        // Height of the board.
    private int totalMines;    // Total number of mines on the board.
    private boolean GameOver;  // State to check if the game is over.
  
     /**
     * Initializes an empty board without mines.
     */
    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new Cell[height][width];
         for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                cells[i][j] = new Cell();
            }
        }
         
         
        
    }
        /**
     * Initializes a board with mines.
     */
    public Board(int width, int height, int totalMines) {
        this.width = width;
        this.height = height;
        this.totalMines = totalMines;
        cells = new Cell[height][width];
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                cells[i][j] = new Cell();
            }
        }
        generateBoard();
    }
    // places the mines in random cordinates on the baord
    private void generateBoard() {
        Random rand = new Random();
        int minesPlaced = 0;
        while (minesPlaced < totalMines) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            if (!cells[y][x].isMine()) {
                cells[y][x].setMine(true);
                // to be removed
                //System.out.println("Placed a mine at: (" + x + "," + y + ")");
                minesPlaced++;
            }
        }
        calculateandSetNeighboringMines();
    }

    // seting the number of mines on empty cells
    private void calculateandSetNeighboringMines() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (!cells[y][x].isMine()) {
                    int mines = countMinesAround(x, y);
                    cells[y][x].setNeighboringMines(mines);
                }
            }
        }
    }
    // counts the amount of mines surrouding a cell
    private int countMinesAround(int x, int y) {
        int count = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int nx = x + dx;
                int ny = y + dy;
                if (nx >= 0 && ny >= 0 && nx < width && ny < height && cells[ny][nx].isMine()) {
                    count++;
                }
            }
        }
        return count;
    }

   
        
        
   //reveals cell at x and y cordinates
public void revealCell(int x, int y, String gameId) {
       
   //return if out of bounds
    if (x < 0 || x >= this.width || y < 0 || y >= this.height || cells[y][x].isRevealed()) {
        //System.out.println("Either out of bounds or already revealed. Skipping cell: (" + x + "," + y + ")");
        return;
    }

    cells[y][x].reveal();
    //System.out.println("Revealing cell: (" + x + "," + y + ")");
    
    // game is lost when mine is revealed
    if (cells[y][x].isMine()) {
        System.out.println("This cell is a mine!");
        GameOver = true;
        return;
        
        
    }
    // whena cell has no neighbouring mines and is revealed, all surrouding cells are also revealed
    if (cells[y][x].getNeighboringMines() == 0) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;

                int newX = x + i;
                int newY = y + j;

                if (newX >= 0 && newX < this.width && newY >= 0 && newY < this.height) {
                    if (!cells[newY][newX].isMine()) {
                       // System.out.println("User trying to reveal: (" + newX + "," + newY + ")");
                        revealCell(newX, newY,gameId);
                        
                      
                    }
                }
            }
        }
    }

        
    }

    //flag or unflaf cell
public void toggleCellFlag(int x, int y, String gameId) {
   

        if (x >= 0 && x < width && y >= 0 && y < height) {
            cells[y][x].toggleFlag();
        }
    }
// set all none mine cells to revealed, mainly used for debugginh
public boolean allNonMineCellsRevealed() {
    int nonMineRevealedCount = 0;
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            if (!cells[y][x].isMine() && cells[y][x].isRevealed()) {
                nonMineRevealedCount++;
            }
        }
    }
    System.out.println("Revealed Count: " + nonMineRevealedCount);
System.out.println("Expected Revealed: " + (width * height - totalMines));
    return nonMineRevealedCount == (width * height - totalMines);
}

//checks is all mines are flagged, this is a game win condition
public boolean allMinesFlagged() {
    
    int mines =countMinesOnBoard();
     if (mines <= 0) {
        return false;  // If no mines are reported, return false immediately
    }
    int correctFlagCount = 0;
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            if (cells[y][x].isMine() && cells[y][x].isFlagged()) {
                correctFlagCount++;
            }
        }
    }
    
    //System.out.println("Correct Flag Count: " + correctFlagCount);
//System.out.println("Total Mines: " + totalMines);
    return correctFlagCount == totalMines;
}

// reveals all mines on the board, this is used when the game is lost, all the mines will be displayed
public void RevealMines() {
    for (int i = 0; i < height; i++) {
       for (int j = 0; j < width; j++) {
           Cell cell = cells[i][j];
            if (cell.isMine()) {
               cell.reveal();
            }
       }
  }
}
   



public void setGameover(boolean GameOver) {
    this.GameOver = GameOver;
}

public boolean getGameover() {
    return this.GameOver;
}

// Getter for width
    public int getWidth() {
        return width;
    }

    // Setter for width
    public void setWidth(int width) {
            this.width = width;
    }

    // Getter for height
    public int getHeight() {
        return height;
    }


    public void setHeight(int height) {
        
            this.height = height;
     
    }
    //converts string representation data of a board into a board object
public static Board fromString(String data) {
    //System.out.println("Attempting to deserialize: " + data);

    StringTokenizer tokenizer = new StringTokenizer(data, ";");
    
    if (!tokenizer.hasMoreTokens()) {
        throw new IllegalArgumentException("Expected dimensions data missing.");
    }
    
    StringTokenizer dimensionsTokenizer = new StringTokenizer(tokenizer.nextToken(), ",");
    //System.out.println("Total dimension tokens: " + dimensionsTokenizer.countTokens());
    
    if (dimensionsTokenizer.countTokens() < 3) {
        throw new IllegalArgumentException("Incomplete dimensions data.");
    }
    
    int width = Integer.parseInt(dimensionsTokenizer.nextToken());
    int height = Integer.parseInt(dimensionsTokenizer.nextToken());
    int expectedTotalMines = Integer.parseInt(dimensionsTokenizer.nextToken());
    
    // Check if the parsed values are reasonable
    if (width <= 0 || height <= 0 || expectedTotalMines < 0 || expectedTotalMines > width * height) {
        throw new IllegalArgumentException("Invalid board dimensions or mine count.");
    }
    
    //System.out.println("Width: " + width + ", Height: " + height + ", Expected Mines: " + expectedTotalMines);
    
    Board board = new Board(width, height);
  
    int actualTotalMines = 0;  // To keep track of the mines placed

    for (int y = 0; y < height; y++) {
        if (!tokenizer.hasMoreTokens()) {
            throw new IllegalArgumentException("Expected row data for row " + y + " missing.");
        }

        StringTokenizer rowTokenizer = new StringTokenizer(tokenizer.nextToken(), ",");
        //System.out.println("Tokens in row " + y + ": " + rowTokenizer.countTokens());
        
        for (int x = 0; x < width; x++) {
            if (!rowTokenizer.hasMoreTokens()) {
                throw new IllegalArgumentException("Expected cell data for row " + y + " column " + x + " missing.");
            }

            if("1".equals(rowTokenizer.nextToken())) {
                board.cells[y][x].setMine(true);
                actualTotalMines++;
                System.out.println("Mine placed at (" + x + ", " + y + ")");
            }
        }
    }

   // System.out.println("Expected mines: " + expectedTotalMines + ", Actual mines placed: " + actualTotalMines);
    
    if (expectedTotalMines != actualTotalMines) {
       // System.out.println("WARNING: Expected number of mines and actual mines placed are different!");
    }
  
    board.calculateandSetNeighboringMines();
    board.setAllCellsToNotFlagged();
    board.setAllCellsToNotRevealed();
    board.updateMinesOnBoard();
    return board;
}
    
    //converts game into string, saving the size, amount of mines and and wether each mine and empty cells are located
   
@Override
public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(width).append(",").append(height).append(",").append(totalMines);
    
    for (int y = 0; y < height; y++) {
        builder.append(";");
        for (int x = 0; x < width; x++) {
            builder.append(cells[y][x].isMine() ? "1" : "0");
            if (x < width - 1) {
                builder.append(",");
            }
        }
    }
    
    return builder.toString();
}
    
 
// return a list of moves
public List<Move> getMoves() {
    return moves;
}
// 
public void clearMoves() {
    moves.clear();
}

public void placeBombsManually(List<String> bombCoordinates) {
    bombCoordinates.stream().map(coordinate -> coordinate.split(",")).forEachOrdered(splitCoordinate -> {
        int row = Integer.parseInt(splitCoordinate[0]);
        int col = Integer.parseInt(splitCoordinate[1]);
        this.cells[row][col].setMine(true);
        });
    // After placing the bombs, update the adjacent bomb counts for all cells.
    calculateandSetNeighboringMines();

}

// used to set this.totalmines when a blank board is created
private void updateMinesOnBoard() {
    int mineCount = 0;

    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            if (cells[y][x].isMine()) {
                mineCount++;
            }
        }
    }

    this.totalMines= mineCount;
}
// returns number of mines on a baord
public int countMinesOnBoard() {
    int mineCount = 0;

    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            if (cells[y][x].isMine()) {
                mineCount++;
            }
        }
    }

    return mineCount;
}
//all cells on the board are set to not reveal, this is used for loading saved boards
public void setAllCellsToNotRevealed() {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            cells[y][x].setRevealed(false);
        }
    }
}
//all cells on the board are set to not flagged, this is used for loading saved boards
public void setAllCellsToNotFlagged() {
    for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
            cells[y][x].setFlagged(false);
        }
    }
    
    
}

//gets a cell at specified coordinates.
public Cell getCell(int x, int y) {
    if (x < 0 || x >= width || y < 0 || y >= height) {
        throw new IllegalArgumentException("Coordinates out of bounds");
    }
    return cells[y][x];
}

}
