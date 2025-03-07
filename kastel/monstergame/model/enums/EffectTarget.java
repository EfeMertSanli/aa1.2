package edu.kit.kastel.monstergame.model.enums;
/**
 * Represents the target of an effect during combat.
 * Determines whether the effect applies to the user or an enemy.
 * @author uuifx
 */
public enum EffectTarget {
    /**
     * The effect targets the monster using the action.
     */
    SELF,
    /**
     * The effect targets an enemy.
     */
    TARGET
}