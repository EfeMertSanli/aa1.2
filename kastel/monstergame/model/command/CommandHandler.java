package edu.kit.kastel.monstergame.model.command;

import edu.kit.kastel.monstergame.model.Monster;
import edu.kit.kastel.monstergame.model.util.FileParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;

package edu.kit.kastel.monstergame.model.command;

import edu.kit.kastel.monstergame.model.command.handlers.*;

/**
 * Main command handler for the monster game.
 * Manages the command loop and delegates to specialized handlers.
 *
 * @author uuifx
 */
public class CommandHandler {
    private final BufferedReader reader;
    private FileParser.GameData gameData;
    private boolean isRunning;
    private boolean inDebugMode;
    private boolean inCompetition;
    private Monster currentMonster;

    // Specialized handlers
    private CompetitionHandler competitionHandler;
    private MonsterDisplayHandler displayHandler;
    private ConfigurationHandler configHandler;
    private ActionHandler actionHandler;

    /**
     * Creates a new CommandHandler.
     *
     * @param initialGameData The initial game data
     * @param debugMode Whether to run in debug mode
     */
    public CommandHandler(FileParser.GameData initialGameData, boolean debugMode) {
        this.gameData = initialGameData;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.isRunning = true;
        this.inCompetition = false;
        this.inDebugMode = debugMode;

        // Initialize handlers
        this.competitionHandler = new CompetitionHandler(this);
        this.displayHandler = new MonsterDisplayHandler(this);
        this.configHandler = new ConfigurationHandler(this);
        this.actionHandler = new ActionHandler(this);
    }

    /**
     * Starts the main command loop.
     */
    public void start() {
        // Original start method implementation
    }

    /**
     * Process a command from the user.
     *
     * @param input The command input string
     */
    private void processCommand(String input) {
        // Delegate to appropriate handler based on command
    }

    // Getters and setters for the handlers to access shared state

    public FileParser.GameData getGameData() {
        return gameData;
    }

    public void setGameData(FileParser.GameData gameData) {
        this.gameData = gameData;
    }

    public boolean isInCompetition() {
        return inCompetition;
    }

    public void setInCompetition(boolean inCompetition) {
        this.inCompetition = inCompetition;
    }

    public Monster getCurrentMonster() {
        return currentMonster;
    }

    public void setCurrentMonster(Monster monster) {
        this.currentMonster = monster;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public boolean isInDebugMode() {
        return inDebugMode;
    }
}
