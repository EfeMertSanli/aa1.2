package edu.kit.kastel.monstergame.model.effect;

import edu.kit.kastel.monstergame.model.enums.DamageType;
import edu.kit.kastel.monstergame.model.enums.EffectTarget;
import edu.kit.kastel.monstergame.model.enums.EffectType;

public class DamageEffect extends Effect {
    private int power;
    private DamageType damageType;

    public DamageEffect(EffectTarget target, DamageType damageType, int power, double hitRate) {
        super(EffectType.DAMAGE, target, hitRate);
        this.power = power;
        this.damageType = damageType;
    }

    public int getPower() {
        return power;
    }

    public DamageType getDamageType() {
        return damageType;
    }

    /**
     * Get a string representation for display in the show actions command
     *
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