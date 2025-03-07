package edu.kit.kastel.monstergame.model.effect;

import edu.kit.kastel.monstergame.model.enums.DamageType;
import edu.kit.kastel.monstergame.model.enums.EffectTarget;
import edu.kit.kastel.monstergame.model.enums.EffectType;
/**
 * Represents a healing effect that can be applied to monsters.
 * Healing effects restore health points to their targets based on different healing types
 * and power values. The amount of healing is calculated differently depending on the
 * healing type specified.
 * @author uuifx
 */
public class HealingEffect extends Effect {
    private int power;
    private DamageType healType; // BASE, RELATIVE, or ABSOLUTE
    /**
     * Creates a new healing effect with the specified parameters.
     * @param hitRate The probability of the effect hitting its target (0.0-1.0)
     * @param target The target of the effect
     * @param power The base power value used for healing calculations
     * @param healType The type of healing that determines how the healing amount is calculated
     */
    public HealingEffect(EffectTarget target, DamageType healType, int power, double hitRate) {
        super(EffectType.HEALING, target, hitRate);
        this.power = power;
        this.healType = healType;
    }
    /**
     * Gets the power value of this healing effect.
     * The power value is used based on the heal type:
     * ABSOLUTE: the HP to restore
     * RELATIVE:  the percentage of max HP to restore
     * BASE: used in formula with users stats to determine healing
     * @return The power value of this healing effect
     */
    public int getPower() {
        return power;
    }
    /**
     * Gets the healing type of this effect.
     * The healing type determines how the healing calculations.
     * @return The healing type of this effect
     */
    public DamageType getHealType() {
        return healType;
    }

    /**
     * Get a string representation for display in the show actions command.
     * @return A string of the representation of the the healing type
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
