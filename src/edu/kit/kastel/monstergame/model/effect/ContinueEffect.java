package edu.kit.kastel.monstergame.model.effect;

import edu.kit.kastel.monstergame.model.enums.EffectTarget;
import edu.kit.kastel.monstergame.model.enums.EffectType;

public class ContinueEffect extends Effect {
    public ContinueEffect(double hitRate) {
        super(EffectType.CONTINUE, EffectTarget.SELF, hitRate);
    }

    @Override
    public String toString() {
        return String.format("%s(hit_rate=%.2f)", effectType.getValue(), hitRate);
    }
}
