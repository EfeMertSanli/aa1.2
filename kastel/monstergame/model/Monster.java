package edu.kit.kastel.monstergame.model;

import edu.kit.kastel.monstergame.model.enums.Element;
import edu.kit.kastel.monstergame.model.enums.ProtectionTarget;
import edu.kit.kastel.monstergame.model.enums.StatType;
import edu.kit.kastel.monstergame.model.enums.StatusCondition;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
/**
 * Represents a monster in the game with stats, actions, and status conditions.
 * @author uuifx
 */
public class Monster {
    private String name;
    private Element element;
    private Map<StatType, Integer> baseStats;
    private Map<StatType, Integer> currentStats;
    private int currentHp;
    private List<Action> actions;
    private StatusCondition statusCondition;
    private Map<StatType, Integer> statStages;
    private Map<ProtectionTarget, Integer> protection;
    private int contestantNumber; // Wettstreiter-Nummer
    private Action selectedAction; // For storing the selected action during combat

    /**
     * Creates a new Monster with the specified attributes.
     *
     * @param name The monster's name
     * @param element The monster's element type
     * @param baseStats The monster's base stats
     * @param actions The list of actions this monster can perform
     */
    public Monster(String name, Element element, Map<StatType, Integer> baseStats, List<Action> actions) {
        this.name = name;
        this.element = element;
        this.baseStats = new EnumMap<>(baseStats);
        this.currentStats = new EnumMap<>(baseStats);
        for (Map.Entry<StatType, Integer> entry : baseStats.entrySet()) {
            this.currentStats.put(entry.getKey(), entry.getValue());
        }
        this.currentHp = baseStats.getOrDefault(StatType.HP, 0);
        this.actions = new ArrayList<>(actions);
        this.statusCondition = null;

        this.statStages = new EnumMap<>(StatType.class);
        for (StatType stat : StatType.values()) {
            if (stat != StatType.HP) {
                statStages.put(stat, 0);
            }
        }

        this.protection = new EnumMap<>(ProtectionTarget.class);
        for (ProtectionTarget target : ProtectionTarget.values()) {
            protection.put(target, 0);
        }
    }


    /**
     * Calculate the effective value for a stat based on its base value and stage modifier.
     * @param statType The type of stat to calculate
     * @return The effective stat value after applying stage modifiers
     */
    public double getEffectiveStat(StatType statType) {
        int baseValue = baseStats.getOrDefault(statType, 0);
        int stage = statStages.getOrDefault(statType, 0);

        // Base value for the formula depends on the stat type
        int b = (statType == StatType.PRC || statType == StatType.AGL) ? 3 : 2;

        // Calculate stage factor
        double stageFactor;
        if (stage >= 0) {
            stageFactor = (double) (b + stage) / b;
        } else {
            stageFactor = (double) b / (b - stage);
        }

        // Calculate effective value before status conditions
        double effectiveValue = baseValue * stageFactor;

        // Apply status condition effects
        if (statusCondition != null) {
            switch (statusCondition) {
                case WET:
                    // WET reduces defense by 25%
                    if (statType == StatType.DEF) {
                        effectiveValue *= 0.75;
                    }
                    break;
                case BURN:
                    // BURN reduces attack by 25%
                    if (statType == StatType.ATK) {
                        effectiveValue *= 0.75;
                    }
                    break;
                case QUICKSAND:
                    // QUICKSAND reduces speed by 25%
                    if (statType == StatType.SPD) {
                        effectiveValue *= 0.75;
                    }
                    break;
                default: break;
            }
        }

        // Ensure the value doesn't go below 1.0
        return Math.max(1.0, effectiveValue);
    }

    /**
     * Get a string describing the monster's current status condition.
     * @return A string of the monsters status
     */
    public String getStatusConditionDisplay() {
        if (isDefeated()) {
            return "FAINTED";
        }

        if (statusCondition == null) {
            return "OK";
        }

        return statusCondition.name();
    }

    /**
     * Modify a stat's stage value, ensuring it stays within -5 to +5 range.
     *
     * @param statType The stat to modify
     * @param change   The number of stages to add/subtract
     */
    public void modifyStat(StatType statType, int change) {
        int currentStage = statStages.getOrDefault(statType, 0);
        int newStage = Math.max(-5, Math.min(5, currentStage + change));
        statStages.put(statType, newStage);
    }

    /**
     * Get the effective speed for determining action order.
     *
     * @return The effective speed value (as an integer for sorting)
     */
    public int getEffectiveSpeed() {
        return (int) getEffectiveStat(StatType.SPD);
    }
    /**
     * Gets the name of the monster.
     * @return The monster's name
     */
    public String getName() {
        return name;
    }
    /**
     * Gets the element of the monster.
     * @return The monster's element
     */
    public Element getElement() {
        return element;
    }
    /**
     * Gets a copy of the monster's base stats.
     * @return A map containing the base stat values
     */
    public Map<StatType, Integer> getBaseStats() {
        return new EnumMap<>(baseStats);
    }
    /**
     * Gets the current HP of the monster.
     * @return The current HP
     */
    public int getCurrentHp() {
        return currentHp;
    }
    /**
     * Sets the current HP of the monster, ensuring it stays within valid range.
     * @param hp The new HP value
     */
    public void setCurrentHp(int hp) {
        this.currentHp = Math.max(0, Math.min(hp, baseStats.getOrDefault(StatType.HP, 0)));
    }
    /**
     * Gets a copy of the monsters available actions.
     * @return A list of actions
     */
    public List<Action> getActions() {
        return new ArrayList<>(actions);
    }
    /**
     * Gets the current status condition affecting the monster.
     * @return The status condition
     */
    public StatusCondition getStatusCondition() {
        return statusCondition;
    }
    /**
     * Sets the monsters status condition.
     * @param condition The new status condition, or null to clear it
     */
    public void setStatusCondition(StatusCondition condition) {
        this.statusCondition = condition;
    }
    /**
     * Gets a copy of the monster's stat stages.
     * @return A map containing the stat stages
     */
    public Map<StatType, Integer> getStatStages() {
        return new EnumMap<>(statStages);
    }
    /**
     * Gets a copy of the monster's protection durations.
     * @return A map containing the protection durations
     */
    public Map<ProtectionTarget, Integer> getProtection() {
        return new EnumMap<>(protection);
    }

    /**
     * Sets protection for a specific target for a number of rounds.
     * @param target The protection target
     * @param rounds The number of rounds the protection lasts
     */
    public void setProtection(ProtectionTarget target, int rounds) {
        protection.put(target, rounds);
    }
    /**
     * Sets the contestant number for the monster in a competition.
     * @param number The contestant number
     */
    public void setContestantNumber(int number) {
        this.contestantNumber = number;
    }
    /**
     * Gets the contestant number of the monster.
     * @return The contestant number
     */
    public int getContestantNumber() {
        return contestantNumber;
    }
    /**
     * Sets the selected action for the monster in the current round.
     * @param action The action to select, or null to pass
     */
    public void setSelectedAction(Action action) {
        this.selectedAction = action;
    }
    /**
     * Gets the currently selected action for the monster.
     * @return The selected action
     */
    public Action getSelectedAction() {
        return selectedAction;
    }
    /**
     * Checks if the monster is defeated.
     * @return true if the monster is defeated, false otherwise
     */
    public boolean isDefeated() {
        return currentHp <= 0;
    }
    /**
     * Returns a string representation of the monster.
     * @return A string containing the monster's name, element, stats, and actions
     */
    @Override
    public String toString() {
        StringBuilder statsStr = new StringBuilder();
        boolean first = true;
        for (Map.Entry<StatType, Integer> entry : baseStats.entrySet()) {
            if (!first) {
                statsStr.append(", ");
            }
            statsStr.append(entry.getKey().getValue()).append(": ").append(entry.getValue());
            first = false;
        }

        StringBuilder actionsStr = new StringBuilder();
        for (Action action : actions) {
            actionsStr.append("\n  ").append(action.getName());
        }

        return String.format("Monster: %s (Element: %s)\nStats: %s\nActions:%s",
                name, element.name(), statsStr.toString(), actionsStr.toString());
    }
}