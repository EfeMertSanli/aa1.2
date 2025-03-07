package edu.kit.kastel.monstergame.model.enums;

/**
 * Represents the possible status conditions that can affect monsters.
 * @author uuifx
 */
public enum StatusCondition {
    /**
     * Causes damage over time.
     */
    BURN,
    /**
     * Reduces defense.
     */
    WET,
    /**
     *  Reduces speed.
     */
    QUICKSAND,
    /**
     * Cannot act for a number of turns.
     */
    SLEEP

}
