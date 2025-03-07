package edu.kit.kastel.monstergame.model.effect;

import edu.kit.kastel.monstergame.model.enums.EffectTarget;
import edu.kit.kastel.monstergame.model.enums.EffectType;
import edu.kit.kastel.monstergame.model.enums.ProtectionTarget;

/**
 * Represents an effect that provides protection against damage or stat reductions.
 * Protection effects shield a monster from either incoming damage or negative stat changes for
 * a specified number of rounds.
 * The protection does not work against self-inflicted effects.
 * The duration of protection can be either fixed or randomly determined within
 * a specified range. When a protection effect ends, a message is displayed.
 * If a monster already has protection of the same type, the new protection replaces
 * the existing one rather than stacking.
 * @author uuifx
 */
public class ProtectionEffect extends Effect {
    private ProtectionTarget protectionTarget;
    private int rounds;
    private int minRounds; // For random rounds range
    private int maxRounds; // For random rounds range
    private boolean randomRounds; // Whether this is a random rounds count

    /**
     * Constructs a new protection effect with a fixed duration.
     * @param protectionTarget The type of protection to provide
     * @param rounds The number of rounds the protection lasts
     * @param hitRate The hit rate of the effect(0.0-1.0)
     */
    public ProtectionEffect(ProtectionTarget protectionTarget, int rounds, double hitRate) {
        super(EffectType.PROTECTION, EffectTarget.SELF, hitRate);
        this.protectionTarget = protectionTarget;
        this.rounds = rounds;
        this.randomRounds = false;
    }

    /**
     * Creates a new protection effect with a random duration within a range.
     * @param protectionTarget The type of protection to provide(health,stat)
     * @param minRounds The minimum number of rounds the protection lasts
     * @param maxRounds The maximum number of rounds the protection lasts
     * @param hitRate The hit rate of the effect (0.0-1.0)
     */
    public ProtectionEffect(ProtectionTarget protectionTarget, int minRounds, int maxRounds, double hitRate) {
        super(EffectType.PROTECTION, EffectTarget.SELF, hitRate);
        this.protectionTarget = protectionTarget;
        this.minRounds = minRounds;
        this.maxRounds = maxRounds;
        this.randomRounds = true;
    }
    /**
     * Gets the protection target(what the effect protects against).
     * @return The protection target(health, stat)
     */
    public ProtectionTarget getProtectionTarget() {
        return protectionTarget;
    }
    /**
     * Gets the fixed duration of protection in rounds.
     * Only valid if this is not a random duration effect.
     * @return The number of rounds the protection lasts
     */
    public int getRounds() {
        return rounds;
    }
    /**
     * Gets the minimum duration for random protection in rounds.
     * Only used if this is a random duration effect.
     * @return The minimum number of rounds the protection lasts
     */
    public int getMinRounds() {
        return minRounds;
    }
    /**
     * Gets the maximum duration for random protection in rounds.
     * Only used if this is a random duration effect.
     * @return The maximum number of rounds the protection lasts
     */
    public int getMaxRounds() {
        return maxRounds;
    }
    /**
     * Checks if this effect uses a random protection duration.
     * @return true if the duration is random, false if it's fixed
     */
    public boolean isRandomRounds() {
        return randomRounds;
    }
    /**
     * Returns a string of the protection effect.
     * @return A string containing the effect type, protection target, duration, and hit rate
     */
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
