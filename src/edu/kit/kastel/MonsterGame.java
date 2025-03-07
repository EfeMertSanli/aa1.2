package edu.kit.kastel;

import edu.kit.kastel.monstergame.model.combat.CombatSystem;
import edu.kit.kastel.monstergame.model.command.CommandHandler;
import edu.kit.kastel.monstergame.model.command.CommandInterface;
import edu.kit.kastel.monstergame.model.command.impl.ConsoleCommandInterface;
import edu.kit.kastel.monstergame.model.Monster;
import edu.kit.kastel.monstergame.model.util.FileParser;
import edu.kit.kastel.monstergame.model.util.RandomUtil;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class MonsterGame {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java -jar MonsterCompetition.jar <config_file_path> [seed|debug]");
        }

        String configFilePath = args[0];

        // Handle optional seed parameter or debug flag
        long seed = -1; // Default: no seed
        boolean debugMode = false;

        if (args.length >= 2) {
            if (args[1].equalsIgnoreCase("debug")) {
                debugMode = true;
                System.out.println("Debug mode enabled");
            } else {
                try {
                    seed = Long.parseLong(args[1]);
                    System.out.println("Random seed: " + seed);
                } catch (NumberFormatException e) {
                    System.out.println("Warning: Second argument is not a valid integer or 'debug'. Using default random seed.");
                }
            }
        }

        // Print the content of the config file
        System.out.println("Contents of " + configFilePath + ":");
        System.out.println("----------------------------------------");
        try {
            java.nio.file.Path path = Paths.get(configFilePath);
            java.util.List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                System.out.println(line);
            }
            System.out.println("----------------------------------------\n");
        } catch (IOException e) {
            System.out.println("Error reading config file: " + e.getMessage());
        }

        // Parse the config file from the path
        FileParser.GameData gameData = FileParser.parseFile(configFilePath);

        if (gameData.getMonsters().isEmpty() || gameData.getActions().isEmpty()) {
            System.out.println("Error: Invalid or empty configuration file.");
        }


        System.out.println("Loaded " + gameData.getActions().size() + " actions, "
                + gameData.getMonsters().size() + " monsters.");
        // Initialize command handler
        CommandHandler commandHandler = new CommandHandler(gameData, debugMode);
        commandHandler.start();
    }
}