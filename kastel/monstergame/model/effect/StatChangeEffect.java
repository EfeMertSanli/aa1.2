package edu.kit.kastel.monstergame.model.effect;
import edu.kit.kastel.monstergame.model.enums.EffectTarget;
import edu.kit.kastel.monstergame.model.enums.EffectType;
import edu.kit.kastel.monstergame.model.enums.StatType;
/**
 * Represents an effect that changes a stat stage of a target monster.
 * Stat change effects can modify stats (ATK, DEF, SPD, PRC, or AGL)
 * by increasing or decreasing their stages. Each monster's stats can be
 * modified from -5 to +5 stages, affecting their effective stat values
 * Stat reductions can be blocked by stat protection effects
 * @author uuifx
 */
public class StatChangeEffect extends Effect {
    private StatType stat;
    private int stages; // Positive for increase, negative for decrease
    /**
     * Creates a new stat change effect with the specified parameters.
     * @param target The target of the effect
     * @param stat The stat to modify
     * @param stages The number of stages to change the stat (positive to increase, negative to decrease)
     * @param hitRate The hit rate of the effect (0.0-1.0)
     */
    public StatChangeEffect(EffectTarget target, StatType stat, int stages, double hitRate) {
        super(EffectType.STAT_CHANGE, target, hitRate);
        this.stat = stat;
        this.stages = stages;
    }
    /**
     * Gets the stat type that this effect modifies.
     * @return The stat type
     */
    public StatType getStat() {
        return stat;
    }
    /**
     * Gets the number of stages to change the stat.
     * @return The stage change value
     */
    public int getStages() {
        return stages;
    }
    /**
     * Returns a string  of the stat change effect.
     * @return A string containing the effect type, target, stat, stages, and hit rate
     */
    @Override
    public String toString() {
        return String.format("%s(target=%s, stat=%s, stages=%d, hit_rate=%.2f)",
                effectType.getValue(), target.name(), stat.name(), stages, hitRate);
    }
}
