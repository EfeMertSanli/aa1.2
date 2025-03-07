package edu.kit.kastel.monstergame.model.combat;

import edu.kit.kastel.monstergame.model.Action;
import edu.kit.kastel.monstergame.model.Monster;

import edu.kit.kastel.monstergame.model.effect.Effect;
import edu.kit.kastel.monstergame.model.effect.RepeatEffect;
import edu.kit.kastel.monstergame.model.enums.EffectTarget;
import edu.kit.kastel.monstergame.model.enums.EffectType;
import edu.kit.kastel.monstergame.model.enums.StatType;
import edu.kit.kastel.monstergame.model.util.RandomUtil;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Handles the execution of actions during combat.
 * @author uuifx
 */
public class ActionExecutor {
    private RandomUtil randomUtil;
    private boolean inDebugMode;
    private StatusConditionHandler statusHandler;
    private EffectProcessor effectProcessor;

    private boolean currentActionHasDamage;
    private boolean firstDamageCalculation;

    /**
     * Creates a new action executor.
     *
     * @param randomUtil Utility for random number generation
     * @param debugMode Whether to run in debug mode
     * @param statusHandler Handler for status conditions
     * @author uuifx
     */
    public ActionExecutor(RandomUtil randomUtil, boolean debugMode, StatusConditionHandler statusHandler) {
        this.randomUtil = randomUtil;
        this.inDebugMode = debugMode;
        this.statusHandler = statusHandler;
        this.effectProcessor = new EffectProcessor(randomUtil, debugMode);
    }

    /**
     * Updates the random utility.
     * @param randomUtil The new random utility
     * @author uuifx
     */
    public void setRandomUtil(RandomUtil randomUtil) {
        this.randomUtil = randomUtil;
        this.effectProcessor.setRandomUtil(randomUtil);
    }

    /**
     * Executes an action for a monster.
     *
     * @param attacker The monster performing the action
     * @param action The action to execute
     * @param monsters List of all monsters in combat
     * @return True if the action succeeded, false otherwise
     * @author uuifx
     */
    public boolean executeAction(Monster attacker, Action action, List<Monster> monsters) {
        currentActionHasDamage = actionHasDamageEffect(action);
        firstDamageCalculation = currentActionHasDamage;
        Queue<Effect> effectQueue = new LinkedList<>();

        // Process the actions effects and add to queue
        processActionEffects(action, effectQueue);

        // Execute effects in queue
        return executeEffects(attacker, effectQueue, monsters);
    }

    /**
     * Check if an action has any damage effects.
     *
     * @param action The action to check
     * @return True if the action has damage effects
     */
    private boolean actionHasDamageEffect(Action action) {
        for (Effect effect : action.getEffects()) {
            if (effect.getEffectType() == EffectType.DAMAGE) {
                return true;
            } else if (effect.getEffectType() == EffectType.REPEAT) {
                RepeatEffect repeatEffect = (RepeatEffect) effect;
                for (Effect repeatedEffect : repeatEffect.getEffects()) {
                    if (repeatedEffect.getEffectType() == EffectType.DAMAGE) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Process the effects of an action and add them to the queue.
     *
     * @param action The action to process
     * @param effectQueue The queue to add effects to
     */
    private void processActionEffects(Action action, Queue<Effect> effectQueue) {
        for (Effect effect : action.getEffects()) {
            if (effect.getEffectType() == EffectType.REPEAT) {
                RepeatEffect repeatEffect = (RepeatEffect) effect;

                // Determine the number of repetitions
                int repeatCount = repeatEffect.getCount();

                // If the count is specified as a range, randomly determine the actual count
                if (repeatEffect.isRandomCount()) {
                    int minCount = repeatEffect.getMinCount();
                    int maxCount = repeatEffect.getMaxCount();
                    repeatCount = randomUtil.getRandomInt(minCount, maxCount, "repeat count");
                }

                // Add repeated effects to queue
                for (int i = 0; i < repeatCount; i++) {
                    for (Effect repeatedEffect : repeatEffect.getEffects()) {
                        effectQueue.add(repeatedEffect);
                    }
                }
            } else {
                effectQueue.add(effect);
            }
        }
    }

    /**
     * Execute the effects in the queue.
     *
     * @param attacker The monster performing the action
     * @param effectQueue Queue of effects to execute
     * @param monsters List of all monsters in combat
     * @return True if the action succeeded, false otherwise
     */
    private boolean executeEffects(Monster attacker, Queue<Effect> effectQueue, List<Monster> monsters) {
        boolean firstEffect = true;

        while (!effectQueue.isEmpty()) {
            Effect effect = effectQueue.poll();

            // Choose target monster based on effect target
            Monster target = determineTarget(attacker, effect, monsters);
            if (target == null) {
                continue;
            }

            boolean hits = calculateHit(attacker, target, effect);

            if (!hits) {
                if (firstEffect) {
                    return false;
                } else {
                    continue;
                }
            }

            // Apply the effect
            boolean isFirstDamage = effect.getEffectType() == EffectType.DAMAGE && firstDamageCalculation;
            if (isFirstDamage) {
                firstDamageCalculation = false;
            }

            effectProcessor.applyEffect(attacker, target, effect, isFirstDamage);

            firstEffect = false;
        }

        return true;
    }

    /**
     * Determine the target for an effect.
     *
     * @param attacker The monster performing the action
     * @param effect The effect to determine the target for
     * @param monsters List of all monsters in combat
     * @return The target monster or null if no target can be found
     */
    private Monster determineTarget(Monster attacker, Effect effect, List<Monster> monsters) {
        if (effect.getTarget() == EffectTarget.SELF) {
            return attacker;
        } else {
            // Note to self: For now works with 1 monster, need to expand
            for (Monster monster : monsters) {
                if (monster != attacker && !monster.isDefeated()) {
                    return monster;
                }
            }
        }
        return null;
    }

    /**
     * Calculate if an effect hits its target.
     *
     * @param attacker The monster performing the action
     * @param target The target monster
     * @param effect The effect to calculate hit for
     * @return True if the effect hits, false otherwise
     */
    private boolean calculateHit(Monster attacker, Monster target, Effect effect) {
        if (attacker.isDefeated() || target.isDefeated()) {
            return false;
        }

        double baseHitRate = effect.getHitRate() * 100; // Convert to percentage
        double hitChance;

        if (effect.getTarget() == EffectTarget.SELF) {
            // Self-targeting effects
            hitChance = baseHitRate * attacker.getEffectiveStat(StatType.PRC);
        } else {
            double attackerPrc = attacker.getEffectiveStat(StatType.PRC);
            double targetAgl = target.getEffectiveStat(StatType.AGL);
            hitChance = baseHitRate * (attackerPrc / targetAgl);
        }

        // Output hit calculation when in debug mode
        if (inDebugMode) {
            if (effect.getTarget() == EffectTarget.SELF) {
                System.out.println("Hit calculation: " + baseHitRate + "% * "
                        + String.format("%.3f", attacker.getEffectiveStat(StatType.PRC))
                        + " (PRC) = " + String.format("%.3f", hitChance) + "%");
            } else {
                System.out.println("Hit calculation: " + baseHitRate + "% * "
                        + String.format("%.3f", attacker.getEffectiveStat(StatType.PRC))
                        + "/" + String.format("%.3f", target.getEffectiveStat(StatType.AGL))
                        + " (PRC/AGL) = " + String.format("%.3f", hitChance) + "%");
            }
        }

        // Use RandomUtil to determine if attack hits
        return randomUtil.rollChance(hitChance, "hit calculation for " + effect.getEffectType());
    }
}