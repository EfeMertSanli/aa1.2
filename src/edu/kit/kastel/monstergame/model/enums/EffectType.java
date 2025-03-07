package edu.kit.kastel.monstergame.model.enums;

/**
 * Represents the different types of effects that actions can have.
 * Each effect type has different behavior and outcomes in combat.
 * @author uuifx
 */
public enum EffectType {
    /**
     * Deals damage to the target.
     */
    DAMAGE("Damage"),

    /**
     * Inflicts a status condition on the target.
     */
    STATUS_CONDITION("StatusCondition"),

    /**
     * Changes the stat stages of the target.
     */
    STAT_CHANGE("StatChange"),

    /**
     * Provides protection against damage or stat changes.
     */
    PROTECTION("Protection"),

    /**
     * Restores health points.
     */
    HEALING("Healing"),

    /**
     * Repeats a set of effects a specified number of times.
     */
    REPEAT("Repeat"),

    /**
     * Continues the action even if the previous effect missed.
     */
    CONTINUE("Continue");

    private final String value;

    /**
     * Constructs an EffectType.
     * @param value The string of the effect type
     */
    EffectType(String value) {
        this.value = value;
    }

    /**
     * Gets the string representation of the effect type.
     * @return The string of the effect type
     */
    public String getValue() {
        return value;
    }
}