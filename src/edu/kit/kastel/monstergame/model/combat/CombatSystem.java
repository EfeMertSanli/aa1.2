package edu.kit.kastel.monstergame.model.combat;

import edu.kit.kastel.monstergame.model.command.CommandInterface;
import edu.kit.kastel.monstergame.model.Action;
import edu.kit.kastel.monstergame.model.Monster;
import edu.kit.kastel.monstergame.model.enums.ProtectionTarget;
import edu.kit.kastel.monstergame.model.enums.StatusCondition;
import edu.kit.kastel.monstergame.model.util.RandomUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Main class responsible for the monster combat system.
 * @author uuifx
 */
public class CombatSystem {
    private List<Monster> monsters;
    private CommandInterface commandInterface;
    private RandomUtil randomUtil;
    private boolean inDebugMode;

    private ActionExecutor actionExecutor;
    private StatusConditionHandler statusHandler;

    private int currentMonsterIndex;
    private boolean allActionsSelected;

    /**
     * Creates a new combat system with the given monsters and command interface.
     * @param monsters List of monsters that will participate in combat
     * @param commandInterface Interface for handling commands
     * @param debugMode Whether to run in debug mode with extra output
     */
    public CombatSystem(List<Monster> monsters, CommandInterface commandInterface, boolean debugMode) {
        this.monsters = new ArrayList<>(monsters);
        this.commandInterface = commandInterface;
        this.inDebugMode = debugMode;
        this.randomUtil = new RandomUtil(0, debugMode); // Default seed 0

        // Initialize helper classes
        this.statusHandler = new StatusConditionHandler(randomUtil, inDebugMode);
        this.actionExecutor = new ActionExecutor(randomUtil, inDebugMode, statusHandler);

        // Assign contestant numbers
        for (int i = 0; i < monsters.size(); i++) {
            monsters.get(i).setContestantNumber(i + 1);
        }

        // Initialize phase tracking
        currentMonsterIndex = 0;
        allActionsSelected = false;
    }

    /**
     * Sets the random seed for the combat system.
     *
     * @param seed The seed to use for random number generation
     */
    public void setRandomSeed(long seed) {
        this.randomUtil = new RandomUtil(seed, inDebugMode);
        this.statusHandler.setRandomUtil(randomUtil);
        this.actionExecutor.setRandomUtil(randomUtil);
    }

    /**
     * Gets a copy of the monster list.
     *
     * @return A new list containing all monsters
     */
    public List<Monster> getMonsters() {
        return new ArrayList<>(monsters);
    }

    /**
     * Gets a list of monsters that are still active in combat.
     *
     * @return List of non-defeated monsters
     */
    public List<Monster> getActiveFighters() {
        List<Monster> active = new ArrayList<>();
        for (Monster monster : monsters) {
            if (!monster.isDefeated()) {
                active.add(monster);
            }
        }
        return active;
    }

    /**
     * Gets the next monster that needs to select an action.
     *
     * @return The next monster or null if all have selected actions
     */
    public Monster getNextMonsterForActionSelection() {
        List<Monster> sortedMonsters = new ArrayList<>(monsters);
        Collections.sort(sortedMonsters, new Comparator<Monster>() {
            @Override
            public int compare(Monster m1, Monster m2) {
                return Integer.compare(m1.getContestantNumber(), m2.getContestantNumber());
            }
        });
        for (Monster monster : sortedMonsters) {
            if (!monster.isDefeated() && monster.getSelectedAction() == null) {
                return monster;
            }
        }
        return null;
    }

    /**
     * Checks if there is a winner in the combat.
     *
     * @return The winning monster, or null if there is no winner yet or it's a draw
     */
    public Monster checkForWinner() {
        List<Monster> activeFighters = getActiveFighters();

        if (activeFighters.size() < 2) {
            if (activeFighters.size() == 1) {
                return activeFighters.get(0);
            } else {
                return null; // Draw
            }
        }

        return null;
    }

    /**
     * Execute the action execution phase.
     */
    public void executeActionsPhase() {
        // Sort monsters by their effective speed
        List<Monster> sortedBySpeed = new ArrayList<>(getActiveFighters());
        Collections.sort(sortedBySpeed, (m1, m2) -> Integer.compare(m2.getEffectiveSpeed(), m1.getEffectiveSpeed()));
        for (Monster attacker : sortedBySpeed) {
            if (!attacker.isDefeated()) {
                Action action = attacker.getSelectedAction();
                if (action == null) {
                    System.out.println("It's " + attacker.getName() + "'s turn.");
                    System.out.println(attacker.getName() + " passes!");
                    if (attacker.getStatusCondition() == StatusCondition.BURN) {
                        statusHandler.applyBurnDamage(attacker);
                    }
                    continue;
                }

                System.out.println("It's " + attacker.getName() + "'s turn.");
                // Check for status conditions
                if (attacker.getStatusCondition() != null) {
                    // Process status condition before action
                    StatusCondition condition = attacker.getStatusCondition();
                    if (condition == StatusCondition.BURN) {
                        System.out.println(attacker.getName() + " is burning!");
                    } else if (condition == StatusCondition.WET) {
                        System.out.println(attacker.getName() + " is soaked!");
                    } else if (condition == StatusCondition.QUICKSAND) {
                        System.out.println(attacker.getName() + " is stuck in quicksand!");
                    } else if (condition == StatusCondition.SLEEP) {
                        System.out.println(attacker.getName() + " is sleeping and cannot move!");
                        // Check if sleep ends
                        boolean conditionEnds = randomUtil.rollChance(33.33, "status condition end for " + attacker.getName());
                        if (conditionEnds) {
                            System.out.println(attacker.getName() + "'s sleeping has faded!");
                            attacker.setStatusCondition(null);
                        } else {
                            // Monster asleep and cannot act
                            continue;
                        }
                    }
                }

                System.out.println(attacker.getName() + " uses " + action.getName() + "!");

                // Execute the action using the ActionExecutor
                boolean actionFailed = !actionExecutor.executeAction(attacker, action, monsters);

                if (actionFailed) {
                    System.out.println("The action failed...");
                }

                // Apply burn damage after action if monster is burning
                if (attacker.getStatusCondition() == StatusCondition.BURN) {
                    statusHandler.applyBurnDamage(attacker);
                }

                attacker.setSelectedAction(null);
            }
        }
    }

    /**
     * Process end of round effects.
     */
    public void endOfRoundPhase() {
        List<Monster> sortedMonsters = new ArrayList<>(monsters);

        Collections.sort(sortedMonsters, new Comparator<Monster>() {
            @Override
            public int compare(Monster m1, Monster m2) {
                return Integer.compare(m1.getContestantNumber(), m2.getContestantNumber());
            }
        });

        for (Monster monster : sortedMonsters) {
            if (!monster.isDefeated()) {
                Map<ProtectionTarget, Integer> protection = monster.getProtection();

                for (ProtectionTarget target : protection.keySet()) {
                    int rounds = protection.get(target);
                    if (rounds > 0) {
                        protection.put(target, rounds - 1);
                        if (protection.get(target) == 0) {
                            System.out.println(monster.getName() + "'s "
                                    + (target == ProtectionTarget.HEALTH ? "damage" : "stat reduction")
                                    + " protection has ended.");
                        }
                    }
                }

                statusHandler.evaluateStatusCondition(monster);
            }
        }
        for (Monster monster : monsters) {
            monster.setSelectedAction(null);
        }
    }

    /**
     * Start a competition with the selected monsters.
     *
     * @return The winning monster or null if it's a draw
     */
    public Monster startCombat() {
        int roundCount = 1;

        while (true) {
            System.out.println("\n=== Round " + roundCount + " ===");

            // Phase 0
            List<Monster> activeFighters = getActiveFighters();
            if (activeFighters.size() < 2) {
                if (activeFighters.size() == 1) {
                    Monster winner = activeFighters.get(0);
                    System.out.println(winner.getName() + " has no opponents left and wins the competition!");
                    return winner;
                } else {
                    System.out.println("No monsters left. It's a draw!");
                    return null;
                }
            }

            // Note to self, rest of the phasing is now in commandhandler

            roundCount++;
        }
    }
}