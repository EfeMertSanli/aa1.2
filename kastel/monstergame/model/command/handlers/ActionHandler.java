package edu.kit.kastel.monstergame.model.command.handlers;

import edu.kit.kastel.monstergame.model.Action;
import edu.kit.kastel.monstergame.model.Monster;
import edu.kit.kastel.monstergame.model.command.CommandHandler;

/**
 * Handles action-related commands during competition.
 *
 * @author uuifx
 */
public class ActionHandler {
    private final CommandHandler commandHandler;
    private boolean waitingForDebugInput;

    /**
     * Creates a new ActionHandler.
     *
     * @param commandHandler The main command handler
     */
    public ActionHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
        this.waitingForDebugInput = false;
    }

    /**
     * Selects an action for the current monster.
     *
     * @param actionName The name of the action
     * @param targetName The name of the target (if needed)
     */
    public void selectAction(String actionName, String targetName) {
        Monster currentMonster = commandHandler.getCurrentMonster();
        if (currentMonster == null) {
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
            System.out.println("Error, " + currentMonster.getName()
                    + " does not know the action " + actionName + ".");
            return;
        }

        currentMonster.setSelectedAction(selectedAction);
        commandHandler.getCompetitionHandler().nextMonsterOrPhase();
    }

    /**
     * Makes the current monster pass its turn.
     */
    public void pass() {
        Monster currentMonster = commandHandler.getCurrentMonster();
        if (currentMonster == null) {
            System.out.println("Error: pass command only available during competition in Phase I");
            return;
        }

        // Set the monster's selected action to null
        currentMonster.setSelectedAction(null);
        commandHandler.getCompetitionHandler().nextMonsterOrPhase();
    }

    /**
     * Checks if the game is waiting for debug input.
     *
     * @return True if waiting for debug input, false otherwise
     */
    public boolean isWaitingForDebugInput() {
        return waitingForDebugInput;
    }

    /**
     * Sets whether the game is waiting for debug input.
     *
     * @param waiting True to set waiting, false otherwise
     */
    public void setWaitingForDebugInput(boolean waiting) {
        this.waitingForDebugInput = waiting;
    }

    /**
     * Handle debug input during combat.
     *
     * @param input The debug input to handle
     */
    public void handleDebugInput(String input) {
        if (input.equalsIgnoreCase("quit")) {
            commandHandler.setRunning(false);
            System.out.println("Exiting game...");
        } else if (input.equalsIgnoreCase("show")) {
            // Show debug information
            System.out.println("Debug information would be shown here");
        } else {
            // Process debug input for random decisions
            // Will be implemented with damage calculations
        }
    }
}