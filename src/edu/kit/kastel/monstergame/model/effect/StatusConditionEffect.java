package edu.kit.kastel.monstergame.model.effect;
import edu.kit.kastel.monstergame.model.enums.EffectTarget;
import edu.kit.kastel.monstergame.model.enums.EffectType;
import edu.kit.kastel.monstergame.model.enums.StatusCondition;

public class StatusConditionEffect extends Effect {
    private StatusCondition condition;

    public StatusConditionEffect(EffectTarget target, StatusCondition condition, double hitRate) {
        super(EffectType.STATUS_CONDITION, target, hitRate);
        this.condition = condition;
    }

    public StatusCondition getCondition() {
        return condition;
    }

    @Override
    public String toString() {
        return String.format("%s(target=%s, condition=%s, hit_rate=%.2f)",
                effectType.getValue(), target.name(), condition.name(), hitRate);
    }
}