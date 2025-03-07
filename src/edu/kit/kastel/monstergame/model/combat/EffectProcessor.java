package edu.kit.kastel.monstergame.model.combat;
import edu.kit.kastel.monstergame.model.Monster;
import edu.kit.kastel.monstergame.model.effect.DamageEffect;
import edu.kit.kastel.monstergame.model.effect.Effect;
import edu.kit.kastel.monstergame.model.effect.StatChangeEffect;
import edu.kit.kastel.monstergame.model.effect.StatusConditionEffect;
import edu.kit.kastel.monstergame.model.effect.ProtectionEffect;
import edu.kit.kastel.monstergame.model.effect.HealingEffect;


import edu.kit.kastel.monstergame.model.enums.ProtectionTarget;
import edu.kit.kastel.monstergame.model.enums.StatType;
import edu.kit.kastel.monstergame.model.enums.StatusCondition;
import edu.kit.kastel.monstergame.model.util.RandomUtil;

import static edu.kit.kastel.monstergame.model.enums.DamageType.BASE;
import static edu.kit.kastel.monstergame.model.enums.DamageType.RELATIVE;
import static edu.kit.kastel.monstergame.model.enums.DamageType.ABSOLUTE;




/**
 * Processes and applies different effect types during combat.
 * @author uuifx
 */
public class EffectProcessor {
    private RandomUtil randomUtil;
    private boolean inDebugMode;
    private DamageCalculator damageCalculator;

    /**
     * Creates a new effect processor.
     * @author uuifx
     * @param randomUtil Utility for random number generation
     * @param debugMode Whether to run in debug mode
     */
    public EffectProcessor(RandomUtil randomUtil, boolean debugMode) {
        this.randomUtil = randomUtil;
        this.inDebugMode = debugMode;
        this.damageCalculator = new DamageCalculator(randomUtil, debugMode);
    }

    /**
     * Updates the random utility.
     * @author uuifx
     * @param randomUtil The new random utility
     */
    public void setRandomUtil(RandomUtil randomUtil) {
        this.randomUtil = randomUtil;
        this.damageCalculator.setRandomUtil(randomUtil);
    }

    /**
     * Apply an effect to its target.
     * @author uuifx
     * @param attacker The monster performing the action
     * @param target The target monster
     * @param effect The effect to apply
     * @param isFirstDamage Whether this is the first damage effect
     */
    public void applyEffect(Monster attacker, Monster target, Effect effect, boolean isFirstDamage) {
        switch (effect.getEffectType()) {
            case DAMAGE:
                applyDamageEffect(attacker, target, (DamageEffect) effect, isFirstDamage);
                break;

            case STATUS_CONDITION:
                applyStatusConditionEffect(attacker, target, (StatusConditionEffect) effect);
                break;

            case STAT_CHANGE:
                applyStatChangeEffect(attacker, target, (StatChangeEffect) effect);
                break;

            case PROTECTION:
                applyProtectionEffect(attacker, target, (ProtectionEffect) effect);
                break;

            case HEALING:
                applyHealingEffect(attacker, target, (HealingEffect) effect);
                break;

            default:
                break;
        }
    }

    /**
     * Apply a damage effect to the target.
     *
     * @param attacker The monster performing the action
     * @param target The target monster
     * @param effect The damage effect to apply
     * @param isFirstDamage Whether this is the first damage effect
     */
    private void applyDamageEffect(Monster attacker, Monster target, DamageEffect effect, boolean isFirstDamage) {
        int damage = 0;
        boolean isProtected = false;

        // Check if the target has protection against damage
        if (target.getProtection().get(ProtectionTarget.HEALTH) > 0 && attacker != target) {
            isProtected = true;
        }

        if (isProtected) {
            System.out.println(target.getName() + " is protected against damage!");
            return;
        }

        // Calculate damage based on damage type
        switch (effect.getDamageType()) {
            case ABSOLUTE:
                damage = effect.getPower();
                break;

            case RELATIVE:
                int maxHp = target.getBaseStats().get(StatType.HP);
                double percentage = effect.getPower() / 100.0;
                damage = (int) Math.ceil(maxHp * percentage);
                break;

            case BASE:
                damage = damageCalculator.calculateBaseDamage(attacker, target, effect, isFirstDamage);
                break;
            default: break;
        }

        // Apply damage to target
        int newHp = target.getCurrentHp() - damage;
        target.setCurrentHp(newHp);

        System.out.println(target.getName() + " takes " + damage + " damage!");
        if (target.isDefeated()) {
            System.out.println(target.getName() + " faints!");
        }
    }

    /**
     * Apply a status condition effect to the target.
     *
     * @param attacker The monster performing the action
     * @param target The target monster
     * @param effect The status condition effect to apply
     */
    private void applyStatusConditionEffect(Monster attacker, Monster target, StatusConditionEffect effect) {
        StatusCondition newCondition = effect.getCondition();

        // If monster already has a condition, it can't get another
        if (target.getStatusCondition() != null) {
            System.out.println(target.getName() + " is already affected by "
                    +
                    target.getStatusCondition() + "!");
            return;
        }

        target.setStatusCondition(newCondition);

        // Apply immediate effects of the condition
        switch (newCondition) {
            case BURN:
                System.out.println(target.getName() + " caught on fire!");
                break;
            case WET:
                System.out.println(target.getName() + " got soaked!");
                break;
            case QUICKSAND:
                System.out.println(target.getName() + " is stuck in quicksand!");
                break;
            case SLEEP:
                System.out.println(target.getName() + " fell asleep!");
                break;
            default: break;
        }
    }

    /**
     * Apply a stat change effect to the target.
     *
     * @param attacker The monster performing the action
     * @param target The target monster
     * @param effect The stat change effect to apply
     */
    private void applyStatChangeEffect(Monster attacker, Monster target, StatChangeEffect effect) {
        StatType statType = effect.getStat();
        int stageChange = effect.getStages();
        boolean isProtected = false;
        if (stageChange < 0 && target.getProtection().get(ProtectionTarget.STATS) > 0 && attacker != target) {
            isProtected = true;
        }

        if (isProtected) {
            System.out.println(target.getName() + " is protected against stat reduction!");
            return;
        }

        // Get current stage values
        int currentStage = target.getStatStages().get(statType);

        target.modifyStat(statType, stageChange);

        int newStage = target.getStatStages().get(statType);

        // Output message based on change
        if (newStage > currentStage) {
            if (attacker == target) {
                System.out.println(target.getName() + "'s " + statType + " rose"
                        + (Math.abs(stageChange) > 1 ? " sharply" : "") + "!");
            } else {
                System.out.println(target.getName() + "'s " + statType
                        + (Math.abs(stageChange) > 1 ? " sharply" : "") + " rose!");
            }
        } else if (newStage < currentStage) {
            if (attacker == target) {
                System.out.println(target.getName() + "'s " + statType
                        + " fell"
                        + (Math.abs(stageChange) > 1 ? " sharply" : "") + "!");
            } else {
                System.out.println(target.getName() + "'s " + statType
                        + (Math.abs(stageChange) > 1 ? " sharply" : "") + " fell!");
            }
        } else {
            // No change (already at max/min)
            System.out.println(target.getName() + "'s " + statType + " cannot go "
                    +
                    (stageChange > 0 ? "higher" : "lower") + "!");
        }
    }

    /**
     * Apply a protection effect to the target.
     *
     * @param attacker The monster performing the action
     * @param target The target monster
     * @param effect The protection effect to apply
     */
    private void applyProtectionEffect(Monster attacker, Monster target, ProtectionEffect effect) {
        ProtectionTarget protectionTarget = effect.getProtectionTarget();
        int rounds;

        // Determine the duration of protection
        if (effect.isRandomRounds()) {
            rounds = randomUtil.getRandomInt(effect.getMinRounds(), effect.getMaxRounds(),
                    "protection duration for " + target.getName());
        } else {
            rounds = effect.getRounds();
        }

        target.setProtection(protectionTarget, rounds);
        System.out.println(target.getName() + " is protected from "
                + (protectionTarget == ProtectionTarget.HEALTH ? "damage" : "stat reductions")
                + " for " + rounds + " rounds!");
    }

    /**
     * Apply healing effect to the target.
     *
     * @param attacker The monster performing the action
     * @param target The target monster
     * @param effect The healing effect to apply
     */
    private void applyHealingEffect(Monster attacker, Monster target, HealingEffect effect) {
        int healAmount = 0;

        switch (effect.getHealType()) {
            case ABSOLUTE:
                healAmount = effect.getPower();
                break;

            case RELATIVE:
                int maxHp = target.getBaseStats().get(StatType.HP);
                double percentage = effect.getPower() / 100.0;
                healAmount = (int) Math.ceil(maxHp * percentage);
                break;

            case BASE:
                double attackerAtk = attacker.getEffectiveStat(StatType.ATK);
                double healBase = effect.getPower() * (attackerAtk / 100.0);
                healAmount = (int) Math.ceil(healBase);
                break;
            default: break;
        }

        // Apply healing to target
        int maxHp = target.getBaseStats().get(StatType.HP);
        int currentHp = target.getCurrentHp();
        int newHp = Math.min(maxHp, currentHp + healAmount);
        int actualHeal = newHp - currentHp;

        target.setCurrentHp(newHp);

        System.out.println(target.getName() + " recovered " + actualHeal + " HP!");
    }
}