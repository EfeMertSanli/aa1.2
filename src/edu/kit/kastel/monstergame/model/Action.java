package edu.kit.kastel.monstergame.model;

import edu.kit.kastel.monstergame.model.effect.Effect;
import edu.kit.kastel.monstergame.model.enums.Element;

import java.util.ArrayList;
import java.util.List;

public class Action {
    private String name;
    private Element element;
    private List<Effect> effects;

    public Action(String name, Element element, List<Effect> effects) {
        this.name = name;
        this.element = element;
        this.effects = new ArrayList<>(effects);
    }

    public String getName() {
        return name;
    }

    public Element getElement() {
        return element;
    }

    public List<Effect> getEffects() {
        return new ArrayList<>(effects);
    }

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