package edu.kit.kastel.monstergame.model.effect;
import edu.kit.kastel.monstergame.model.enums.EffectTarget;
import edu.kit.kastel.monstergame.model.enums.EffectType;
/**
 * Base class for all effects.
 * Effects represent different actions or status changes that can occur during battle(damage, healing,
 * stat changes, status conditions, protection, repeated)
 * Each effect has a type, hit rate, and target.
 * @author uuifx
 */
public abstract class Effect {
    protected EffectType effectType;
    protected EffectTarget target;
    protected double hitRate;
    /**
     * Creates a new effect with the specified parameters.
     * @param effectType The type of this effect
     * @param hitRate The probability of the effect hitting its target (0.0-1.0)
     * @param target The target of the effect
     */
    public Effect(EffectType effectType, EffectTarget target, double hitRate) {
        this.effectType = effectType;
        this.target = target;
        this.hitRate = hitRate;
    }
    /**
     * Gets the type of this effect.
     * The effect type determines how the effect is processed
     * @return The type of this effect
     */
    public EffectType getEffectType() {
        return effectType;
    }
    /**
     * Gets the target of this effect.
     * The target determines whom the effect applies to.
     * @return The target of this effect
     */
    public EffectTarget getTarget() {
        return target;
    }
    /**
     * Gets the hit rate of this effect.
     * The hit rate represents the base probability of the effect hitting its target
     * without considering precision evasion.
     * @return The hit rate value 0.0-1.0
     */
    public double getHitRate() {
        return hitRate;
    }

    @Override
    public String toString() {
        return String.format("%s(target=%s, hit_rate=%.2f)", effectType.getValue(), target.name(), hitRate);
    }
}
