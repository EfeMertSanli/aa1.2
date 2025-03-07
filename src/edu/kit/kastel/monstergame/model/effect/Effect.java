package edu.kit.kastel.monstergame.model.effect;

// Base class for all effects

import edu.kit.kastel.monstergame.model.enums.EffectTarget;
import edu.kit.kastel.monstergame.model.enums.EffectType;

public abstract class Effect {
    protected EffectType effectType;
    protected EffectTarget target;
    protected double hitRate;

    public Effect(EffectType effectType, EffectTarget target, double hitRate) {
        this.effectType = effectType;
        this.target = target;
        this.hitRate = hitRate;
    }

    public EffectType getEffectType() {
        return effectType;
    }

    public EffectTarget getTarget() {
        return target;
    }

    public double getHitRate() {
        return hitRate;
    }

    @Override
    public String toString() {
        return String.format("%s(target=%s, hit_rate=%.2f)", effectType.getValue(), target.name(), hitRate);
    }
}
