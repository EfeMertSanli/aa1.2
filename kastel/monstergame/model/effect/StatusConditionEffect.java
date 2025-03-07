package edu.kit.kastel.monstergame.model.effect;
import edu.kit.kastel.monstergame.model.enums.EffectTarget;
import edu.kit.kastel.monstergame.model.enums.EffectType;
import edu.kit.kastel.monstergame.model.enums.StatusCondition;
/**
 * Represents an effect that inflicts a status condition on a target monster.
 * Status condition effects can apply conditions: BURN, WET, QUICKSAND, or SLEEP
 * A monster can only be affected by one status condition at a time. If a monster
 * already has a status condition, attempts to apply another will fail.
 * @author uuifx
 */
public class StatusConditionEffect extends Effect {
    private StatusCondition condition;
    /**
     * Creates a new status condition effect with the specified parameters.
     * @param target The target of the effect
     * @param condition The status condition to inflict
     * @param hitRate The hit rate of the effect (0.0-1.0)
     */
    public StatusConditionEffect(EffectTarget target, StatusCondition condition, double hitRate) {
        super(EffectType.STATUS_CONDITION, target, hitRate);
        this.condition = condition;
    }
    /**
     * Gets the status condition that this effect inflicts.
     * @return The status condition
     */
    public StatusCondition getCondition() {
        return condition;
    }
    /**
     * Returns a string of the status condition effect.
     * @return A string containing the effect type, target, condition, and hit rate
     */
    @Override
    public String toString() {
        return String.format("%s(target=%s, condition=%s, hit_rate=%.2f)",
                effectType.getValue(), target.name(), condition.name(), hitRate);
    }
}