package edu.kit.kastel.monstergame.model.util;

import edu.kit.kastel.monstergame.model.enums.Element;

/**
 * Utility class to calculate element effectiveness between different elements.
 * @author uuifx
 */
public final class ElementEffectiveness {


    /**
     * Private constructor to prevent instantiation.
     */
    private ElementEffectiveness() {
    }

    /**
     * Determines if the attacking element is super effective against the defending element.
     *
     * WATER is super effective against FIRE
     * FIRE is super effective against EARTH
     * EARTH is super effective against WATER
     *
     * @param attackingElement The element of the attacking move
     * @param defendingElement The element of the defending monster
     * @return true if super effective, false otherwise
     */
    public static boolean isSuperEffective(Element attackingElement, Element defendingElement) {
        return (attackingElement == Element.WATER && defendingElement == Element.FIRE)
                || (attackingElement == Element.FIRE && defendingElement == Element.EARTH)
                || (attackingElement == Element.EARTH && defendingElement == Element.WATER);
    }

    /**
     * Determines if the attacking element is not very effective against the defending element.
     * @param attackingElement The element of the attacking action
     * @param defendingElement The element of the defending monster
     * @return true if not very effective false otherwise
     * @author uuifx
     */
    public static boolean isNotVeryEffective(Element attackingElement, Element defendingElement) {
        return isSuperEffective(defendingElement, attackingElement);
    }

    /**
     * Determines if the attacking element has normal effectiveness against the defending element.
     * (when either element is NORMAL or both elements are the same)
     * @param attackingElement The element of the attacking action
     * @param defendingElement The element of the defending monster
     * @return true if normal effectiveness false otherwise
     * @author uuifx
     */
    public static boolean isNormalEffective(Element attackingElement, Element defendingElement) {
        return attackingElement == Element.NORMAL
                || defendingElement == Element.NORMAL
                || attackingElement == defendingElement;
    }

    /**
     * Get the element effectiveness factor for damage calculation.
     * @param attackingElement The element of the attacking action
     * @param defendingElement The element of the defending monster
     * @return 2.0 if super effective, 0.5 if not very effective, 1.0 otherwise
     * @author uuifx
     */
    public static double getElementFactor(Element attackingElement, Element defendingElement) {
        if (isSuperEffective(attackingElement, defendingElement)) {
            return 2.0;
        } else if (isNotVeryEffective(attackingElement, defendingElement)) {
            return 0.5;
        } else {
            return 1.0;
        }
    }
}