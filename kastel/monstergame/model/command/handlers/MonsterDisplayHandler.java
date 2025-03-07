package edu.kit.kastel.monstergame.model.command.handlers;

import edu.kit.kastel.monstergame.model.Action;
import edu.kit.kastel.monstergame.model.Monster;
import edu.kit.kastel.monstergame.model.command.CommandHandler;
import edu.kit.kastel.monstergame.model.effect.DamageEffect;
import edu.kit.kastel.monstergame.model.effect.Effect;
import edu.kit.kastel.monstergame.model.effect.RepeatEffect;
import edu.kit.kastel.monstergame.model.enums.EffectType;
import edu.kit.kastel.monstergame.model.enums.StatType;

import java.util.List;
import java.util.Map;

/**
 * Handles display-related commands for showing monster information.
 *
 * @author uuifx
 */
public class MonsterDisplayHandler {
    private final CommandHandler commandHandler;

    /**
     * Creates a new MonsterDisplayHandler.
     *
     * @param commandHandler The main command handler
     */
    public MonsterDisplayHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    /**
     * Shows all monsters in the current competition.
     */
    public void showCompetitionMonsters() {
        if (!commandHandler.isInCompetition()) {
            System.out.println("Error: show command only available during competition");
            return;
        }

        List<Monster> monsters = commandHandler.getCombatSystem().getMonsters();

        for (Monster monster : monsters) {
            displayMonsterStatus(monster);
        }
    }

    /**
     * Displays a monster's status including health bar.
     *
     * @param monster The monster to display status for
     */
    private void displayMonsterStatus(Monster monster) {
        int maxHp = monster.getBaseStats().get(StatType.HP);
        int currentHp = monster.getCurrentHp();
        int m = (int) Math.round(20.0 * currentHp / maxHp);
        int n = 20 - m;

        // Build the health bar
        StringBuilder healthBar = new StringBuilder("[");
        for (int i = 0; i < m; i++) {
            healthBar.append("X");
        }
        for (int i = 0; i < n; i++) {
            healthBar.append("_");
        }
        healthBar.append("]");

        // Add asterisk to current monster's name
        String asterisk = (monster == commandHandler.getCurrentMonster()) ? "*" : "";
        String statusText = monster.getStatusConditionDisplay();

        System.out.println(healthBar + " " + monster.getContestantNumber() + " "
                + asterisk + monster.getName() + " (" + statusText + ")");
    }

    /**
     * Shows all available monsters in the game.
     */
    public void showAllMonsters() {
        List<Monster> monsters = commandHandler.getGameData().getMonsters();

        for (Monster monster : monsters) {
            Map<StatType, Integer> stats = monster.getBaseStats();
            System.out.println(monster.getName() + ": Element " + monster.getElement()
                    + ", HP " + stats.get(StatType.HP)
                    + ", ATK " + stats.get(StatType.ATK)
                    + ", DEF " + stats.get(StatType.DEF)
                    + ", SPD " + stats.get(StatType.SPD));
        }
    }

    /**
     * Shows all actions of the current monster.
     */
    public void showActions() {
        if (!commandHandler.isInCompetition() || commandHandler.getCurrentMonster() == null) {
            System.out.println("Error: show actions command only available during competition in Phase I");
            return;
        }

        Monster currentMonster = commandHandler.getCurrentMonster();
        System.out.println("ACTIONS OF " + currentMonster.getName());

        for (Action action : currentMonster.getActions()) {
            displayActionInfo(action);
        }
    }

    /**
     * Displays information about an action.
     *
     * @param action The action to display information for
     */
    private void displayActionInfo(Action action) {
        String damageInfo = "--";
        double hitRate = 0.0;
        boolean hitRateFound = false;

        for (Effect effect : action.getEffects()) {
            if (effect.getEffectType() == EffectType.DAMAGE) {
                DamageEffect damageEffect = (DamageEffect) effect;
                damageInfo = damageEffect.getDamageDisplay();
                if (!hitRateFound) {
                    hitRate = effect.getHitRate() * 100; // Convert to percentage
                    hitRateFound = true;
                }
                break;
            } else if (effect.getEffectType() == EffectType.REPEAT) {
                RepeatEffect repeatEffect = (RepeatEffect) effect;
                if (!repeatEffect.getEffects().isEmpty()) {
                    Effect firstRepeatEffect = repeatEffect.getEffects().get(0);
                    if (firstRepeatEffect.getEffectType() == EffectType.DAMAGE) {
                        DamageEffect damageEffect = (DamageEffect) firstRepeatEffect;
                        damageInfo = damageEffect.getDamageDisplay();
                    }
                    if (!hitRateFound) {
                        hitRate = firstRepeatEffect.getHitRate() * 100; // Convert to percentage
                        hitRateFound = true;
                    }
                    break;
                }
            } else if (!hitRateFound) {
                hitRate = effect.getHitRate() * 100; // Convert to percentage
                hitRateFound = true;
            }
        }

        System.out.println(action.getName() + ": ELEMENT " + action.getElement()
                + ", Damage " + damageInfo + ", HitRate " + (hitRateFound ? (int) hitRate : "--"));
    }

    /**
     * Shows all stats of the current monster.
     */
    public void showStats() {
        if (!commandHandler.isInCompetition() || commandHandler.getCurrentMonster() == null) {
            System.out.println("Error: show stats command only available during competition in Phase I");
            return;
        }

        Monster currentMonster = commandHandler.getCurrentMonster();
        System.out.println("STATS OF " + currentMonster.getName());

        Map<StatType, Integer> baseStats = currentMonster.getBaseStats();
        Map<StatType, Integer> statStages = currentMonster.getStatStages();

        StringBuilder stats = new StringBuilder();

        stats.append("HP ").append(currentMonster.getCurrentHp()).append("/")
                .append(baseStats.get(StatType.HP)).append(", ");

        for (StatType stat : new StatType[]{StatType.ATK, StatType.DEF, StatType.SPD,
                StatType.PRC, StatType.AGL}) {
            stats.append(stat.name()).append(" ").append(baseStats.get(stat));

            int stage = statStages.get(stat);
            if (stage != 0) {
                stats.append("(").append(stage > 0 ? "+" : "").append(stage).append(")");
            }

            if (stat != StatType.AGL) {
                stats.append(", ");
            }
        }

        System.out.println(stats.toString());
    }
}