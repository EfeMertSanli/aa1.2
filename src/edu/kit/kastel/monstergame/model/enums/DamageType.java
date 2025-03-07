package edu.kit.kastel.monstergame.model.enums;


/**
 * Represents different damage types that are used in the game.
 * @author uuifx
 */
public enum DamageType {
    /**
     * Base damage, calculated using the damage formula with stats.
     */
    BASE,
    /**
     * Percentage of max HP.
     */
    RELATIVE,
    /**
     * Fixed damage, not subject to stat modifiers.
     */
    ABSOLUTE
}
