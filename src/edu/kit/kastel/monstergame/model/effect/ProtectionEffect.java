package edu.kit.kastel.monstergame.model.effect;

import edu.kit.kastel.monstergame.model.enums.EffectTarget;
import edu.kit.kastel.monstergame.model.enums.EffectType;
import edu.kit.kastel.monstergame.model.enums.ProtectionTarget;

/**
 * Effect that provides protection from damage or stat changes for a number of rounds.
 * ///To be implemented yet, protection types lacking
 *
 * @author uuifx
 */
public class ProtectionEffect extends Effect {
    private ProtectionTarget protectionTarget;
    private int rounds;
    private int minRounds; // For random rounds range
    private int maxRounds; // For random rounds range
    private boolean randomRounds; // Whether this is a random rounds count

    /**
     * Constructor for a fixed number of rounds
     *
     * @author uuifx
     */
    public ProtectionEffect(ProtectionTarget protectionTarget, int rounds, double hitRate) {
        super(EffectType.PROTECTION, EffectTarget.SELF, hitRate);
        this.protectionTarget = protectionTarget;
        this.rounds = rounds;
        this.randomRounds = false;
    }

    /**
     * Constructor for a random number of rounds within a range
     *
     * @author uuifx
     */
    public ProtectionEffect(ProtectionTarget protectionTarget, int minRounds, int maxRounds, double hitRate) {
        super(EffectType.PROTECTION, EffectTarget.SELF, hitRate);
        this.protectionTarget = protectionTarget;
        this.minRounds = minRounds;
        this.maxRounds = maxRounds;
        this.randomRounds = true;
    }

    public ProtectionTarget getProtectionTarget() {
        return protectionTarget;
    }

    public int getRounds() {
        return rounds;
    }

    public int getMinRounds() {
        return minRounds;
    }

    public int getMaxRounds() {
        return maxRounds;
    }

    public boolean isRandomRounds() {
        return randomRounds;
    }

    @Override
    public String toString() {
        if (randomRounds) {
            return String.format("%s(protection_target=%s, rounds=%d-%d, hit_rate=%.2f)",
                    effectType.getValue(), protectionTarget.name(), minRounds, maxRounds, hitRate);
        } else {
            return String.format("%s(protection_target=%s, rounds=%d, hit_rate=%.2f)",
                    effectType.getValue(), protectionTarget.name(), rounds, hitRate);
        }
    }
}
