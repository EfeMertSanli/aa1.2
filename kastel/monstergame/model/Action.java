package edu.kit.kastel.monstergame.model;

import edu.kit.kastel.monstergame.model.effect.Effect;
import edu.kit.kastel.monstergame.model.enums.Element;

import java.util.ArrayList;
import java.util.List;
/**
 * Represents an action that a monster can perform during combat.
 * Each action has a name, an element type, and a list of effects that are applied
 * when the action is used. The element of an action determines its effectiveness
 * against monsters of different elements. Effects are executed in sequence when
 * an action is performed.
 * @author uuifx
 */
public class Action {
    private String name;
    private Element element;
    private List<Effect> effects;
    /**
     * Creates a new Action with the specified attributes.
     * @param name The name of the action
     * @param element The element type of the action
     * @param effects The list of effects that this action applies when used
     */
    public Action(String name, Element element, List<Effect> effects) {
        this.name = name;
        this.element = element;
        this.effects = new ArrayList<>(effects);
    }
    /**
     * Gets the name of the action.
     * @return The action's name
     */
    public String getName() {
        return name;
    }
    /**
     * Gets the element type of the action.
     * @return The action's element
     */
    public Element getElement() {
        return element;
    }
    /**
     * Gets a copy of the effects that this action applies when used.
     * @return A list of effects
     */
    public List<Effect> getEffects() {
        return new ArrayList<>(effects);
    }
    /**
     * Returns a string representation of the action.
     * @return A string containing the actions name, element, and effects
     */
    @Override
    public String toString() {
        StringBuilder effectsStr = new StringBuilder();
        for (Effect effect : effects) {
            effectsStr.append("\n  ").append(effect.toString());
        }
        return String.format("Action: %s (Element: %s)%s",
                name, element.name(), effectsStr.toString());
    }
}