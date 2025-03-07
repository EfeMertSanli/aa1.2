package edu.kit.kastel.monstergame.model.effect;
import edu.kit.kastel.monstergame.model.enums.EffectTarget;
import edu.kit.kastel.monstergame.model.enums.EffectType;
import java.util.ArrayList;
import java.util.List;

public class RepeatEffect extends Effect {
    private int count;
    private int minCount; // For random count range
    private int maxCount; // For random count range
    private boolean randomCount; // Whether this is a random count
    private List<Effect> effects;

    /**
     * Constructor for a fixed repetition count
     * @author uuifx
     */
    public RepeatEffect(int count, List<Effect> effects) {
        super(EffectType.REPEAT, EffectTarget.SELF, 1.0);
        this.count = count;
        this.effects = new ArrayList<>(effects);
        this.randomCount = false;
    }

    /**
     * Constructor for a random repetition count within a range
     * @author uuifx
     */
    public RepeatEffect(int minCount, int maxCount, List<Effect> effects) {
        super(EffectType.REPEAT, EffectTarget.SELF, 1.0);
        this.minCount = minCount;
        this.maxCount = maxCount;
        this.effects = new ArrayList<>(effects);
        this.randomCount = true;
    }

    public int getCount() {
        return count;
    }

    public int getMinCount() {
        return minCount;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public boolean isRandomCount() {
        return randomCount;
    }

    public List<Effect> getEffects() {
        return new ArrayList<>(effects);
    }

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