 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minesweepertest1;

/**
 *
 * @author grish
 */
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * * @author Grisham Balloo 20099072
 */
public class UserInterface {

    private static final Scanner scanner = new Scanner(System.in);
    private static GameManager gameManager;
    
    /**
     *
     * @param gameManager
     */
    public UserInterface(GameManager gameManager) {
        UserInterface.gameManager = gameManager;
    }

    /**
     *
     * @return
     */
    public static  String promptAction() {
        System.out.println("Enter your action:");
        System.out.println("R [x] [y] - Reveal cell at [x,y]");
        System.out.println("F [x] [y] - Flag cell at [x,y]");
        System.out.println("S - Save Game");
        System.out.println("Q - Quit Game");
        return scanner.nextLine().toUpperCase();
    }

    /**
     *
     * @param combinedInput
     */
    public static void handleAction(String combinedInput) {
        String[] parts = combinedInput.split(" ");
        String actionType = parts[0];

        switch (actionType) {
            case "R":
                if (parts.length != 3) {
                    System.out.println("Invalid input for reveal. Please use the format: R [x] [y]");
                    return;
                }
                int x,y;

                try {
                    x = Integer.parseInt(parts[1]);
                    y = Integer.parseInt(parts[2]);

                    gameManager.RevealCell(x, y);

                } catch (NumberFormatException e) {
                    System.out.println("Invalid coordinates provided for reveal.");
                }
                break;
            case "F":
                if (parts.length != 3) {
                    System.out.println("Invalid input for flag. Please use the format: F [x] [y]");
                    return;
                }
                try {
                    x = Integer.parseInt(parts[1]);
                    y = Integer.parseInt(parts[2]);

                    gameManager.FlagCell(x, y);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid coordinates provided for flag.");

                }
                break;
            case "L":
                gameManager.loadPlayersGame();
                break;
            case "S":
                gameManager.Save();
                System.out.println("Save Successful.");
                break;
            case "Q":

                System.out.println("Would you like to save before quiting?(Y/N)");
                String ans = scanner.nextLine();
                
                if (ans.equalsIgnoreCase("n")) {
                    System.exit(0);
                }
                if (ans.equalsIgnoreCase("y")) {
                    gameManager.Save();
                    System.out.println("Save Successful.");
                     System.out.println("Goodbye!");
                    System.exit(0);
                }

                break;
            
            default:
                System.out.println("Unknown action type.");
                break;
        }
    }

    
    public static DifficultySettings selectGameModeAndSize() {
    DifficultySettings chosenDifficulty = null;

    while (chosenDifficulty == null) {
    System.out.println("Select game difficulty (Easy, Medium, Hard, Q to Quit):");
    String input = scanner.nextLine().trim().toLowerCase();

    switch (input) {
        case "easy":
        case "e":
            chosenDifficulty = DifficultySettings.EASY;
            break;
        case "medium":
        case "m":
            chosenDifficulty = DifficultySettings.MEDIUM;
            break;
        case "hard":
        case "h":
            chosenDifficulty = DifficultySettings.HARD;
            break;
        case "q":
            System.exit(0);  // Exit the program if the user wants to quit
        default:
            System.out.println("Invalid difficulty setting. Please try again.");
    }
    }
        GameManager.chosenDifficulty = DifficultySettings.EASY;
    // Let user choose a size/configuration for the board
    int[][] configurations = chosenDifficulty.getConfigurations();
    System.out.println("Available sizes for " + chosenDifficulty + " mode:");
    for (int i = 0; i < configurations.length; i++) {
        System.out.println((i + 1) + ". " + configurations[i][0] + "x" + configurations[i][1] + " with " + configurations[i][2] + " mines");
    }
    System.out.println("Choose a size (enter the number):");

    int choice = -1;
    while (choice < 1 || choice > configurations.length) {
        try {
            choice = scanner.nextInt();
            if (choice < 1 || choice > configurations.length) {
                System.out.println("Invalid choice. Please choose a valid number.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number.");
            scanner.nextLine();  // clear the invalid input
        }
    }

    int[] selectedConfig = configurations[choice - 1];
    GameManager.BoardManager(selectedConfig[0], selectedConfig[1], selectedConfig[2]);  
    scanner.nextLine();
    return chosenDifficulty;
}

    
    public static int MainMenu() {

        while (true) {
            System.out.println("Welcome to Minesweeper!");
            System.out.println("Enter player name:");

            GameManager.NewPlayer();
            // keep prompting until a valid input is given
            System.out.println("1. New Game");
            System.out.println("2. Load Game");
            System.out.println("3. Dsiplaye Leaderboard (for fastest level completed)");
            System.out.print("Enter your choice: ");
            
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice == 1 || choice == 2|| choice == 3) {
                    return choice;
                } else {
                    System.out.println("Invalid choice. Please select 1 or 2 or 3");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine();  // clear the invalid input
            }
        }
    }
    
   public static void displayLeaderboard() {
    while (true) {
        System.out.println("View Leaderboard for which difficulty? (Easy, Medium, Hard or Q to Quit):");
        String input = scanner.nextLine().trim().toLowerCase();

        switch (input) {
            case "easy":
            case "e":
                GameManager.showLeaderboardForDifficulty(DifficultySettings.EASY);
                break;
            case "medium":
            case "m":
                GameManager.showLeaderboardForDifficulty(DifficultySettings.MEDIUM);
                break;
            case "hard":
            case "h":
                GameManager.showLeaderboardForDifficulty(DifficultySettings.HARD);
                break;
            case "q":
                return;  //  exit the leaderboard 
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
}
   
}
