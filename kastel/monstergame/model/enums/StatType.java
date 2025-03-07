package edu.kit.kastel.monstergame.model.enums;

/**
 * Represents the different types of stats that monsters have.
 * Stats influence combat and they can be modified by status conditions or effects.
 *
 * @author uuifx
 */
public enum StatType {
    /**
     * Health Points. Determines how much damage a monster can take before fainting.
     */
    HP("HP"),

    /**
     * Attack. Determines the attack power of a monster.
     */
    ATK("ATK"),

    /**
     * Defense. Reduces damage taken from attacks.
     */
    DEF("DEF"),

    /**
     * Speed. Determines turn order in combat and affects critical hit chances.
     */
    SPD("SPD"),

    /**
     * Precision. Affects accuracy of attacks and abilities.
     */
    PRC("PRC"),

    /**
     * Agility. Affects evasion and ability to avoid attacks.
     */
    AGL("AGL");

    private final String value;

    /**
     * Constructs a StatType with the specified display value.
     *
     * @param value The string representation of the stat
     */
    StatType(String value) {
        this.value = value;
    }

    /**
     * Gets a string representation of the stat.
     *
     * @return The string value of the stat
     */
    public String getValue() {
        return value;
    }

    /**
     * Converts a string to the StatType.
     *
     * @param text The string to convert
     * @return The corresponding StatType
     * @throws IllegalArgumentException if no matching StatType is found
     */
    public static StatType fromString(String text) {
        for (StatType stat : StatType.values()) {
            if (stat.getValue().equalsIgnoreCase(text)) {
                return stat;
            }
        }
        throw new IllegalArgumentException("No stat type with text " + text + " found");
    }
}