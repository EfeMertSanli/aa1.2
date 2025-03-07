package edu.kit.kastel.monstergame.model.command.handlers;
import edu.kit.kastel.monstergame.model.command.CommandHandler;
import edu.kit.kastel.monstergame.model.util.FileParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Handles configuration-related commands like loading files and quitting.
 *
 * @author uuifx
 */
public class ConfigurationHandler {
    private final CommandHandler commandHandler;

    /**
     * Creates a new ConfigurationHandler.
     *
     * @param commandHandler The main command handler
     */
    public ConfigurationHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    /**
     * Handles the quit command.
     */
    public void quit() {
        commandHandler.setRunning(false);
        System.out.println("Exiting game...");
    }

    /**
     * Handles the load command.
     *
     * @param filePath Path to the configuration file
     */
    public void load(String filePath) {
        System.out.println("Loading configuration from: " + filePath);

        try {
            // Print the content of the config file
            java.nio.file.Path path = Paths.get(filePath);
            java.util.List<String> lines = Files.readAllLines(path);

            for (String line : lines) {
                System.out.println(line);
            }

            // Parse the file
            FileParser.GameData newGameData = FileParser.parseFile(filePath);

            if (newGameData.getMonsters().isEmpty() && newGameData.getActions().isEmpty()) {
                System.out.println("Error loading configuration: Invalid format or empty file");
                return;
            }

            // End any current competition
            commandHandler.setInCompetition(false);
            commandHandler.setCurrentMonster(null);

            // Update game data
            commandHandler.setGameData(newGameData);

            System.out.println("\nLoaded " + newGameData.getActions().size() + " actions, "
                    + newGameData.getMonsters().size() + " monsters.");

        } catch (IOException e) {
            System.out.println("Error loading configuration: " + e.getMessage());
        }
    }
}