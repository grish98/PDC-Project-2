/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * @author Grisham Balloo 20099072


 */
package minesweepertest1;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileIO {
 
    private static  String PLAYER_PROFILE_FILE = "./resources/player_profiles.txt";
    private static  String GAME_STATE_FILE = "./resources/game_states.txt";
    private static String MOVE_LOG_FILE = "./resources/move_logs.txt";
    private static String LEADERBOARD_FILE = "./resources/leaderboard.txt";

    
    /**
     * Loads all player profiles from the specified file.
     * 
     * return A list of player profiles.
     */
 private static List<PlayerProfile> loadAllPlayerProfiles() {
    List<PlayerProfile> profiles = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(PLAYER_PROFILE_FILE))) {
        String line;
        while ((line = reader.readLine()) != null) {
            profiles.add(PlayerProfile.fromString(line));
        }
    } catch (IOException e) {
        System.out.println("Error loading player profiles: " + e.getMessage());
    }

    return profiles;
}

 
 
 
    
    public static PlayerProfile loadPlayerProfile(String playerName) {
    try (BufferedReader reader = new BufferedReader(new FileReader(PLAYER_PROFILE_FILE))) {
        String line;
        while ((line = reader.readLine()) != null) {
            PlayerProfile profile = PlayerProfile.fromString(line);
            if (profile.getPlayerName().equals(playerName)) {
                reader.close();
                return profile;
            }
             
        }
        reader.close();
        
    }
            catch(FileNotFoundException e) {
            System.out.println("File not found.");
            }
            catch(IOException e) {
            System.out.println("Error reading from file " + PLAYER_PROFILE_FILE);
            }
    
    return null; // Return null if profile not found
}

    
    /**
     * Saves a player's profile. If the profile already exists,it's updated ,if it doesnt it is saved.The player's profile to save.
     */
    
    public static void savePlayerProfile(PlayerProfile profile) {
        List<PlayerProfile> profiles = loadAllPlayerProfiles();
        boolean profileExists = false;

        // Check if the profile exists and update it.
        for (int i = 0; i < profiles.size(); i++) {
            if (profiles.get(i).getPlayerName().equals(profile.getPlayerName())) {
                profiles.set(i, profile); // Replace the old profile with the updated one.
                profileExists = true;
                break;
            }
        }

        // If the profile doesn't exist, add it to the list.
        if (!profileExists) {
            profiles.add(profile);
        }

        // Now, save all the profiles back to the file.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PLAYER_PROFILE_FILE))) {
            for (PlayerProfile p : profiles) {
                writer.write(p.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving player profile: " + e.getMessage());
        }
    }
    
    // Using Map for GameState,  Saves the current game state.
    public static void saveGameState(GameState gameState) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(GAME_STATE_FILE, true))) {
            writer.write(gameState.toString());
            writer.newLine();
            writer.close();
        } catch (IOException e) {
             System.out.println("Error saving GameState: " + e.getMessage());
        }
    }
//Loads all saved games for a specific player.
    public static Map<String, GameState> loadGameState(String playerName) {
        Map<String, GameState> gameStatesMap = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(GAME_STATE_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                GameState gameState = GameState.fromString(line);
                if (gameState.getPlayerName().equals(playerName)) {
                gameStatesMap.put(gameState.getGameId(), gameState);
                }
            }
            reader.close();
        } catch(FileNotFoundException e) {
            System.out.println("File not found.");
            }
            catch(IOException e) {
            System.out.println("Error reading from file " + GAME_STATE_FILE);
            }
        
        return gameStatesMap;
    }

     public static void saveMoveLog(List<Move> moves) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MOVE_LOG_FILE, true))) {
            for (Move move : moves) {
                writer.write(move.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving move logs: " + e.getMessage());
        }
    }

    public static List<Move> loadMoveLog() {
        List<Move> moves = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(MOVE_LOG_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Move move = Move.fromString(line);
                moves.add(move);
            }
        } catch (IOException e) {
            System.out.println("Error loading move logs: " + e.getMessage());
        }

        return moves;
    }
    
  public static void saveToLeaderboard(Leaderboard newEntry) {
    List<Leaderboard> currentLeaderboard = loadLeaderboard();
    List<Leaderboard> updatedLeaderboard = new ArrayList<>();

    // Filter for the same difficulty as the new entry.
    List<Leaderboard> sameDifficulty = currentLeaderboard.stream()
            .filter(entry -> entry.getDifficulty().equals(newEntry.getDifficulty()))
            .sorted(Comparator.comparingLong(Leaderboard::getTime))
            .collect(Collectors.toList());

    if (sameDifficulty.size() < 3) {
        sameDifficulty.add(newEntry);
    } else {
        if (newEntry.getTime() < sameDifficulty.get(2).getTime()) { // Check against the worst time of top 3.
            sameDifficulty.remove(2); // Remove the last (worst) entry
            sameDifficulty.add(newEntry);
        }
    }

    // Re-sort after addition
    sameDifficulty.sort(Comparator.comparingLong(Leaderboard::getTime));

    // Add the top 3 (or fewer) of the same difficulty back to the updated leaderboard.
    updatedLeaderboard.addAll(sameDifficulty);

        // Add all other difficulties back to the updated leaderboard.
        currentLeaderboard.stream().filter(entry -> (!entry.getDifficulty().equals(newEntry.getDifficulty()))).forEachOrdered(entry -> {
            updatedLeaderboard.add(entry);
        });

    // Save the updated leaderboard back to file.
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(LEADERBOARD_FILE, false))) { // Overwrite mode
        for (Leaderboard entry : updatedLeaderboard) {
            writer.write(entry.toString());
            writer.newLine();
        }
    } catch (IOException e) {
        System.out.println("Error saving to leaderboard: " + e.getMessage());
    }
}
   public static List<Leaderboard> loadLeaderboard() {
    List<Leaderboard> leaderboard = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(new FileReader(LEADERBOARD_FILE))) {
        String line;
        while ((line = reader.readLine()) != null) {
            Leaderboard entry = Leaderboard.fromString(line);
            if (entry != null) {
                leaderboard.add(entry);
            }
        }
    } catch (IOException e) {
        System.out.println("Error loading leaderboard: " + e.getMessage());
    }

    return leaderboard;
}
}