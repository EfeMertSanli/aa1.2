package edu.kit.kastel.monstergame.model.effect;

import edu.kit.kastel.monstergame.model.enums.DamageType;
import edu.kit.kastel.monstergame.model.enums.EffectTarget;
import edu.kit.kastel.monstergame.model.enums.EffectType;

public class HealingEffect extends Effect {
    private int power;
    private DamageType healType; // BASE, RELATIVE, or ABSOLUTE

    public HealingEffect(EffectTarget target, DamageType healType, int power, double hitRate) {
        super(EffectType.HEALING, target, hitRate);
        this.power = power;
        this.healType = healType;
    }

    public int getPower() {
        return power;
    }

    public DamageType getHealType() {
        return healType;
    }

    /**
     * Get a string representation for display in the show actions command
     *
     * @author uuifx
     */
    public String getHealingDisplay() {
        String prefix;
        switch (healType) {
            case BASE:
                prefix = "b";
                break;
            case RELATIVE:
                prefix = "r";
                break;
            case ABSOLUTE:
                prefix = "a";
                break;
            default:
                prefix = "";
                break;
        }
        return prefix + power;
    }

    @Override
    public String toString() {
        return String.format("%s(target=%s, type=%s, power=%d, hit_rate=%.2f)",
                effectType.getValue(), target.name(), healType, power, hitRate);
    }
}
