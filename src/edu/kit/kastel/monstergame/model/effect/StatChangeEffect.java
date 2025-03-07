package edu.kit.kastel.monstergame.model.effect;
import edu.kit.kastel.monstergame.model.enums.EffectTarget;
import edu.kit.kastel.monstergame.model.enums.EffectType;
import edu.kit.kastel.monstergame.model.enums.StatType;

public class StatChangeEffect extends Effect {
    private StatType stat;
    private int stages; // Positive for increase, negative for decrease

    public StatChangeEffect(EffectTarget target, StatType stat, int stages, double hitRate) {
        super(EffectType.STAT_CHANGE, target, hitRate);
        this.stat = stat;
        this.stages = stages;
    }

    public StatType getStat() {
        return stat;
    }

    public int getStages() {
        return stages;
    }

    @Override
    public String toString() {
        return String.format("%s(target=%s, stat=%s, stages=%d, hit_rate=%.2f)",
                effectType.getValue(), target.name(), stat.name(), stages, hitRate);
    }
}
