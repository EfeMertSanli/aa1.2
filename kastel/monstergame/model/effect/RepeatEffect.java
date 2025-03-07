package edu.kit.kastel.monstergame.model.effect;

import edu.kit.kastel.monstergame.model.enums.EffectTarget;
import edu.kit.kastel.monstergame.model.enums.EffectType;

import java.util.ArrayList;
import java.util.List;
/**
 * Represents an effect that repeats a sequence of effects multiple times.
 * Repeat effects act as containers for other effects, allowing those effects
 * to be executed multiple times when an action is performed.
 *
 * When processed, the repeat effect is replaced by multiple instances of its contained effects in
 * the execution queue.
 * @author uuifx
 */
public class RepeatEffect extends Effect {
    private int count;
    private int minCount; // For random count range
    private int maxCount; // For random count range
    private boolean randomCount; // Whether this is a random count
    private List<Effect> effects;

    /**
     * Creates a new repeat effect with a fixed repetition count.
     * @param count The number of times to repeat the effects
     * @param effects The list of effects to repeat
     */
    public RepeatEffect(int count, List<Effect> effects) {
        super(EffectType.REPEAT, EffectTarget.SELF, 1.0);
        this.count = count;
        this.effects = new ArrayList<>(effects);
        this.randomCount = false;
    }

    /**
     * Creates a new repeat effect with a random repetition count within a range.
     * @param minCount The minimum number of repetitions
     * @param maxCount The maximum number of repetitions
     * @param effects The list of effects to repeat
     */
    public RepeatEffect(int minCount, int maxCount, List<Effect> effects) {
        super(EffectType.REPEAT, EffectTarget.SELF, 1.0);
        this.minCount = minCount;
        this.maxCount = maxCount;
        this.effects = new ArrayList<>(effects);
        this.randomCount = true;
    }
    /**
     * Gets the fixed repetition count.
     * Only valid if this is not a random count effect.
     * @return The fixed repetition count
     */
    public int getCount() {
        return count;
    }
    /**
     * Gets the minimum repetition count for random repetitions.
     * Only used if this is a random count effect.
     * @return The minimum repetition count
     */
    public int getMinCount() {
        return minCount;
    }
    /**
     * Gets the maximum repetition count for random repetitions.
     * Only used if this is a random count effect.
     * @return The maximum repetition count
     */
    public int getMaxCount() {
        return maxCount;
    }
    /**
     * Checks if this effect uses a random repetition count.
     * @return true if the repetition count is random, false if its fixed
     */
    public boolean isRandomCount() {
        return randomCount;
    }
    /**
     * Gets a copy of the effects that will be repeated.
     * @return The list of effects to repeat
     */
    public List<Effect> getEffects() {
        return new ArrayList<>(effects);
    }
    /**
     * Returns a string of the repeat effect.
     * @return A string containing the effect type, repetition count, and contained effects
     */
    @Override
    public String toString() {
        StringBuilder effectsStr = new StringBuilder();
        for (int i = 0; i < effects.size(); i++) {
            if (i > 0) {
                effectsStr.append(", ");
            }
            effectsStr.append(effects.get(i).toString());
        }

        if (randomCount) {
            return String.format("%s(count=%d-%d, effects=[%s])",
                    effectType.getValue(), minCount, maxCount, effectsStr.toString());
        } else {
            return String.format("%s(count=%d, effects=[%s])",
                    effectType.getValue(), count, effectsStr.toString());
        }
    }
}