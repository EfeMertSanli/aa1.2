package edu.kit.kastel.monstergame.model.command;

import edu.kit.kastel.monstergame.model.combat.CombatSystem;
import edu.kit.kastel.monstergame.model.Action;
import edu.kit.kastel.monstergame.model.Monster;
import edu.kit.kastel.monstergame.model.command.impl.ConsoleCommandInterface;
import edu.kit.kastel.monstergame.model.effect.DamageEffect;
import edu.kit.kastel.monstergame.model.effect.Effect;
import edu.kit.kastel.monstergame.model.effect.RepeatEffect;
import edu.kit.kastel.monstergame.model.enums.EffectType;
import edu.kit.kastel.monstergame.model.enums.StatType;
import edu.kit.kastel.monstergame.model.util.FileParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Handles all command input and execution for the game
 *
 * @author uuifx
 */
public class CommandHandler {
    private final BufferedReader reader;
    private FileParser.GameData gameData;
    private CombatSystem combatSystem;
    private boolean isRunning;
    private boolean inDebugMode;

    // Game phase tracking
    private boolean inCompetition;
    private Monster currentMonster;

    public CommandHandler(FileParser.GameData initialGameData, boolean debugMode) {
        this.gameData = initialGameData;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
        this.isRunning = true;
        this.inCompetition = false;
        this.inDebugMode = debugMode;
    }

    /**
     * Starts the main command loop
     *
     * @author uuifx
     */
    public void start() {
        while (isRunning) {
            try {
                if (inCompetition && currentMonster != null) {
                    System.out.println("What should " + currentMonster.getName() + " do?");
                }

                System.out.print("> ");
                String input = reader.readLine().trim();
                processCommand(input);

            } catch (IOException e) {
                System.out.println("Error reading input: " + e.getMessage());
            }
        }
    }

    /**
     * Process a command from the user.
     *
     * @param input The command input string
     */
    private void processCommand(String input) {
        if (input.isEmpty()) {
            return;
        }

        // Split the input into command and arguments
        String[] parts = input.split("\\s+");
        String command = parts[0].toLowerCase();

        // Check if in debug mode special input handling
        if (inDebugMode && inCompetition && isWaitingForDebugInput()) {
            handleDebugInput(input);
            return;
        }

        // Process based on command
        switch (command) {
            case "quit":
                commandQuit();
                break;

            case "load":
                if (parts.length < 2) {
                    System.out.println("Error: load command requires a file path");
                } else {
                    commandLoad(parts[1]);
                }
                break;

            case "competition":
                if (parts.length < 3) {
                    System.out.println("Error: competition command requires at least two monster names");
                } else {
                    String[] monsterNames = new String[parts.length - 1];
                    System.arraycopy(parts, 1, monsterNames, 0, parts.length - 1);
                    commandCompetition(monsterNames);
                }
                break;

            case "show":
                if (parts.length == 1) {
                    // Just "show" in competition mode shows monster status
                    if (inCompetition) {
                        commandShow();
                    } else {
                        System.out.println("Error: show command requires additional parameters (monsters, actions, stats)");
                    }
                } else if (parts[1].equalsIgnoreCase("monsters")) {
                    commandShowMonsters();
                } else if (parts[1].equalsIgnoreCase("actions")) {
                    if (inCompetition && currentMonster != null) {
                        commandShowActions();
                    } else {
                        System.out.println("Error: can only show actions during competition in Phase I");
                    }
                } else if (parts[1].equalsIgnoreCase("stats")) {
                    if (inCompetition && currentMonster != null) {
                        commandShowStats();
                    } else {
                        System.out.println("Error: can only show stats during competition in Phase I");
                    }
                } else {
                    System.out.println("Error: unknown show command: " + parts[1]);
                }
                break;

            case "action":
                if (!inCompetition || currentMonster == null) {
                    System.out.println("Error: action command only available during competition in Phase I");
                    return;
                }

                if (parts.length < 2) {
                    System.out.println("Error: action command requires an action name");
                } else {
                    String actionName = parts[1];
                    String targetName = null;

                    // Check if a target is specified and needed
                    if (parts.length > 2) {
                        targetName = parts[2];
                    }

                    commandAction(actionName, targetName);
                }
                break;

            case "pass":
                if (!inCompetition || currentMonster == null) {
                    System.out.println("Error: pass command only available during competition in Phase I");
                } else {
                    commandPass();
                }
                break;

            default:
                System.out.println("Error: unknown command: " + command);
                break;
        }
    }

    /**
     * Quit command: Exits the game
     */
    private void commandQuit() {
        isRunning = false;
        System.out.println("Exiting game...");
    }

    /**
     * Load command: Loads a new configuration file
     */
    private void commandLoad(String filePath) {
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
            inCompetition = false;
            currentMonster = null;

            // Update game data
            gameData = newGameData;

            System.out.println("\nLoaded " + gameData.getActions().size() + " actions, " +
                    gameData.getMonsters().size() + " monsters.");

        } catch (IOException e) {
            System.out.println("Error loading configuration: " + e.getMessage());
        }
    }

    /**
     * Competition command: Starts a new competition with the specified monsters
     */
    private void commandCompetition(String[] monsterNames) {
        if (monsterNames.length < 2) {
            System.out.println("Error: competition requires at least two monsters");
            return;
        }

        List<Monster> competitionMonsters = new ArrayList<>();

        // Keep track of monster name counts (for adding #n suffixes)
        Map<String, Integer> monsterCounts = new java.util.HashMap<>();

        // Check if all monster names are valid and create copies for the competition
        for (String name : monsterNames) {
            boolean found = false;

            for (Monster monster : gameData.getMonsters()) {
                if (monster.getName().equals(name)) {
                    found = true;

                    // Create a deep copy of the monster for the competition
                    Monster competitionMonster = new Monster(
                            monster.getName(),
                            monster.getElement(),
                            monster.getBaseStats(),
                            monster.getActions(),
                            monster.getDescription()
                    );

                    // Check if this monster name appears multiple times
                    monsterCounts.put(name, monsterCounts.getOrDefault(name, 0) + 1);
                    int instanceNumber = monsterCounts.get(name);

                    // If this is a duplicate monster name, add the #n suffix
                    if (instanceNumber > 1) {
                        competitionMonster = new Monster(
                                monster.getName() + "#" + instanceNumber,
                                monster.getElement(),
                                monster.getBaseStats(),
                                monster.getActions(),
                                monster.getDescription()
                        );
                    }

                    competitionMonsters.add(competitionMonster);
                    break;
                }
            }

            if (!found) {
                System.out.println("Error: unknown monster: " + name);
                return;
            }
        }

        // Initialize combat system with the selected monsters
        combatSystem = new CombatSystem(competitionMonsters, new ConsoleCommandInterface(this), inDebugMode);

        System.out.println("The " + competitionMonsters.size() + " monsters enter the competition!");

        // Start the competition
        inCompetition = true;

        // Move to Phase I - Action selection
        startPhaseI();
    }

    /**
     * Show command: Displays all monsters in the current competition
     */
    private void commandShow() {
        if (!inCompetition) {
            System.out.println("Error: show command only available during competition");
            return;
        }

        List<Monster> monsters = combatSystem.getMonsters();

        for (Monster monster : monsters) {
            int maxHp = monster.getBaseStats().get(StatType.HP);
            int currentHp = monster.getCurrentHp();
            int m = (int) Math.round(20.0 * currentHp / maxHp);
            int n = 20 - m;

            // Build the health bar
            StringBuilder healthBar = new StringBuilder("[");
            for (int i = 0; i < m; i++) {
                healthBar.append("X");
            }
            for (int i = 0; i < n; i++) {
                healthBar.append("_");
            }
            healthBar.append("]");

            // Add asterisk to current monster's name
            String asterisk = (monster == currentMonster) ? "*" : "";
            String statusText = monster.getStatusConditionDisplay();

            System.out.println(healthBar + " " + monster.getContestantNumber() + " " +
                    asterisk + monster.getName() + " (" + statusText + ")");
        }
    }

    /**
     * Show monsters command, Shows all available monsters in the game
     *
     * @author uuifx
     */
    private void commandShowMonsters() {
        List<Monster> monsters = gameData.getMonsters();

        for (Monster monster : monsters) {
            Map<StatType, Integer> stats = monster.getBaseStats();
            System.out.println(monster.getName() + ": Element " + monster.getElement() +
                    ", HP " + stats.get(StatType.HP) +
                    ", ATK " + stats.get(StatType.ATK) +
                    ", DEF " + stats.get(StatType.DEF) +
                    ", SPD " + stats.get(StatType.SPD));
        }
    }

    /**
     * Show actions command, Shows all actions of the current monster
     *
     * @author uuifx
     */
    private void commandShowActions() {
        if (!inCompetition || currentMonster == null) {
            System.out.println("Error: show actions command only available during competition in Phase I");
            return;
        }

        System.out.println("ACTIONS OF " + currentMonster.getName());

        for (Action action : currentMonster.getActions()) {
            String damageInfo = "--";
            double hitRate = 0.0;
            boolean hitRateFound = false;
            for (Effect effect : action.getEffects()) {
                if (effect.getEffectType() == EffectType.DAMAGE) {
                    DamageEffect damageEffect = (DamageEffect) effect;
                    damageInfo = damageEffect.getDamageDisplay();
                    if (!hitRateFound) {
                        hitRate = effect.getHitRate() * 100; // Convert to percentage
                        hitRateFound = true;
                    }
                    break;
                } else if (effect.getEffectType() == EffectType.REPEAT) {
                    RepeatEffect repeatEffect = (RepeatEffect) effect;
                    if (!repeatEffect.getEffects().isEmpty()) {
                        Effect firstRepeatEffect = repeatEffect.getEffects().get(0);
                        if (firstRepeatEffect.getEffectType() == EffectType.DAMAGE) {
                            DamageEffect damageEffect = (DamageEffect) firstRepeatEffect;
                            damageInfo = damageEffect.getDamageDisplay();
                        }
                        if (!hitRateFound) {
                            hitRate = firstRepeatEffect.getHitRate() * 100; // Convert to percentage
                            hitRateFound = true;
                        }
                        break;
                    }
                } else if (!hitRateFound) {
                    hitRate = effect.getHitRate() * 100; // Convert to percentage
                    hitRateFound = true;
                }
            }

            System.out.println(action.getName() + ": ELEMENT " + action.getElement() +
                    ", Damage " + damageInfo + ", HitRate " + (hitRateFound ? (int) hitRate : "--"));
        }
    }

    /**
     * Show stats command, Shows all stats of the current monster
     *
     * @author uuifx
     */
    private void commandShowStats() {
        if (!inCompetition || currentMonster == null) {
            System.out.println("Error: show stats command only available during competition in Phase I");
            return;
        }

        System.out.println("STATS OF " + currentMonster.getName());

        Map<StatType, Integer> baseStats = currentMonster.getBaseStats();
        Map<StatType, Integer> statStages = currentMonster.getStatStages();

        StringBuilder stats = new StringBuilder();

        stats.append("HP ").append(currentMonster.getCurrentHp()).append("/")
                .append(baseStats.get(StatType.HP)).append(", ");

        for (StatType stat : new StatType[]{StatType.ATK, StatType.DEF, StatType.SPD, StatType.PRC, StatType.AGL}) {
            stats.append(stat.name()).append(" ").append(baseStats.get(stat));

            int stage = statStages.get(stat);
            if (stage != 0) {
                stats.append("(").append(stage > 0 ? "+" : "").append(stage).append(")");
            }

            if (stat != StatType.AGL) {
                stats.append(", ");
            }
        }

        System.out.println(stats.toString());
    }

    /**
     * Action command: Selects an action of the current monste
     *
     * @author uuifx
     */
    private void commandAction(String actionName, String targetName) {
        if (!inCompetition || currentMonster == null) {
            return;
        }

        Action selectedAction = null;
        for (Action action : currentMonster.getActions()) {
            if (action.getName().equals(actionName)) {
                selectedAction = action;
                break;
            }
        }

        if (selectedAction == null) {
            System.out.println("Error, " + currentMonster.getName() + " does not know the action " + actionName + ".");
            return;
        }
        currentMonster.setSelectedAction(selectedAction);
        nextMonsterOrPhase();
    }

    /**
     * Pass command, current monster passes its turn
     *
     * @author uuifx
     */
    private void commandPass() {
        if (!inCompetition || currentMonster == null) {
            System.out.println("Error: pass command only available during competition in Phase I");
            return;
        }

        // Set the monster's selected action to null
        currentMonster.setSelectedAction(null);

        nextMonsterOrPhase();
    }

    /**
     * Start Phase 1 of the competition
     *
     * @author uuifx
     */
    private void startPhaseI() {
        // Set current monster to the first active monster
        currentMonster = combatSystem.getNextMonsterForActionSelection();
    }

    /**
     * Move to the next monster or phase after action selection
     *
     * @author uuifx
     */
    private void nextMonsterOrPhase() {
        // Get the next monster
        currentMonster = combatSystem.getNextMonsterForActionSelection();

        // If no more monsters need to select actions, move to Phase 2
        if (currentMonster == null) {
            startPhaseII();
        }
    }

    /**
     * Start Phase 2 of the competition
     *
     * @author uuifx
     */
    private void startPhaseII() {
        // Execute all actions
        combatSystem.executeActionsPhase();

        // Check if the competition is over
        Monster winner = combatSystem.checkForWinner();
        if (winner != null) {
            // Competition is over
            System.out.println("\n=== Contest Results ===");
            if (winner != null) {
                System.out.println("Winner: " + winner.getName());
            } else {
                System.out.println("The contest ended in a draw!");
            }

            inCompetition = false;
            currentMonster = null;
        } else {
            // Start a new round
            combatSystem.endOfRoundPhase();
            startPhaseI();
        }
    }

    /**
     * Check if the game is waiting for debug input
     *
     * @author uuifx
     */
    private boolean isWaitingForDebugInput() {
        return false;
    }

    /**
     * Handle debug input during combat
     *
     * @author uuifx
     */
    private void handleDebugInput(String input) {
        if (input.equalsIgnoreCase("quit")) {
            commandQuit();
        } else if (input.equalsIgnoreCase("show")) {
            // Show debug information
            System.out.println("Debug information would be shown here");
        } else {
            // Process debug input for random decisions
            // Will be implemented with damage calculations
        }
    }

    /**
     * Update the current monster
     *
     * @author uuifx
     */
    public void setCurrentMonster(Monster monster) {
        this.currentMonster = monster;
    }
}
