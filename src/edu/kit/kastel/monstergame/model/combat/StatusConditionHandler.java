package edu.kit.kastel.monstergame.model.combat;

import edu.kit.kastel.monstergame.model.Monster;
import edu.kit.kastel.monstergame.model.enums.StatType;
import edu.kit.kastel.monstergame.model.enums.StatusCondition;
import edu.kit.kastel.monstergame.model.util.RandomUtil;

/**
 * Handles status conditions and their effects during combat.
 * @author uuifx
 */
public class StatusConditionHandler {
    private RandomUtil randomUtil;
    private boolean inDebugMode;

    /**
     * Creates a new status condition handler.
     * @param randomUtil Utility for random number generation
     * @param debugMode Whether to run in debug mode
     * @author uuifx
     */
    public StatusConditionHandler(RandomUtil randomUtil, boolean debugMode) {
        this.randomUtil = randomUtil;
        this.inDebugMode = debugMode;
    }

    /**
     * Updates the random utility.
     * @param randomUtil The new random utility.
     * @author uuifx
     */
    public void setRandomUtil(RandomUtil randomUtil) {
        this.randomUtil = randomUtil;
    }

    /**
     * Apply burn damage to a monster.
     * @author uuifx
     * @param monster The monster to apply burn damage to
     * @author uuifx
     */
    public void applyBurnDamage(Monster monster) {
        int maxHp = monster.getBaseStats().get(StatType.HP);
        int burnDamage = (int) Math.ceil(maxHp * 0.1); // 10% of max HP
        int newHp = monster.getCurrentHp() - burnDamage;
        monster.setCurrentHp(newHp);

        System.out.println(monster.getName() + " takes " + burnDamage + " damage from burning!");

        // Check if monster is defeated by burn
        if (monster.isDefeated()) {
            System.out.println(monster.getName() + " faints!");
        }
    }

    /**
     * Evaluate the status condition of a monster and check if it ends.
     *
     * @param monster The monster to evaluate status condition of
     * @return True if the status condition ends false otherwise
     * @author uuifx
     */
    public boolean evaluateStatusCondition(Monster monster) {
        StatusCondition condition = monster.getStatusCondition();
        if (condition != null) {
            // 1/3 chance to end the status condition
            boolean conditionEnds = randomUtil.rollChance(33.33, "status condition end for " + monster.getName());

            if (conditionEnds) {
                String conditionName = "";
                switch (condition) {
                    case BURN:
                        conditionName = "burning";
                        break;
                    case WET:
                        conditionName = "soaked";
                        break;
                    case QUICKSAND:
                        conditionName = "quicksand";
                        break;
                    case SLEEP:
                        conditionName = "sleeping";
                        break;
                    default: break;
                }
                System.out.println(monster.getName() + "'s " + conditionName + " has faded!");
                monster.setStatusCondition(null);
                return true;
            }
        }
        return false;
    }
}