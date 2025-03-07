package edu.kit.kastel.monstergame.model.enums;
/**
 * Represents the different elements that monsters and actions can have.
 * Elements determine effectiveness in combat.
 * @author uuifx
 */
public enum Element {
    /**
     * Normal element. Has no special effectiveness against any other element.
     */
    NORMAL,
    /**
     * Fire element. Effective against Earth, weak against Water.
     */
    FIRE,
    /**
     * Water element. Effective against Fire, weak against Earth.
     */
    WATER,
    /**
     * Earth element. Effective against Water, weak against Fire.
     */
    EARTH
}
