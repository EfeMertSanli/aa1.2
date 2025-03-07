package edu.kit.kastel.monstergame.model.effect;

import edu.kit.kastel.monstergame.model.enums.DamageType;
import edu.kit.kastel.monstergame.model.enums.EffectTarget;
import edu.kit.kastel.monstergame.model.enums.EffectType;
/**
 * Represents damage dealin.
 * Damage effects reduce the targets HP based on different damage types and calculations.
 * The amount of damage is determined by the damage type, power value,
 * and combat factors (stats, elements, critical hits).
 * @author uuifx
 */
public class DamageEffect extends Effect {
    private int power;
    private DamageType damageType;
    /**
     * Creates a new damage effect with the specified parameters.
     * @param hitRate The probability of the effect hitting its target (0.0-1.0)
     * @param target The target of the effect
     * @param power The base power value used for damage calculations
     * @param damageType The type of damage that determines the damage calculation.
     */
    public DamageEffect(EffectTarget target, DamageType damageType, int power, double hitRate) {
        super(EffectType.DAMAGE, target, hitRate);
        this.power = power;
        this.damageType = damageType;
    }
    /**
     * Gets the power value of this damage effect.
     * The power value calculation is based on the damage type:
     * ABSOLUTE: HP to reduce
     * RELATIVE: percentage of max HP to reduce
     * BASE: used in a formula with attacker and defender stats in mind
     * @return The power value of this damage effect
     */
    public int getPower() {
        return power;
    }
    /**
     * Gets the damage type of this effect.
     * The damage type determines the damage calculations.
     * @return The damage type of this effect
     */
    public DamageType getDamageType() {
        return damageType;
    }

    /**
     * Get a string of the display in the show actions command.
     * @return a string that represents the type of damage
     * @author uuifx
     */
    public String getDamageDisplay() {
        String prefix;
        switch (damageType) {
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
                effectType.getValue(), target.name(), damageType, power, hitRate);
    }
}