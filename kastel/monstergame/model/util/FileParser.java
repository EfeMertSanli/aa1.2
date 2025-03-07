package edu.kit.kastel.monstergame.model.util;

import edu.kit.kastel.monstergame.model.Action;
import edu.kit.kastel.monstergame.model.Monster;
import edu.kit.kastel.monstergame.model.effect.Effect;
import edu.kit.kastel.monstergame.model.effect.ProtectionEffect;
import edu.kit.kastel.monstergame.model.effect.StatChangeEffect;
import edu.kit.kastel.monstergame.model.effect.DamageEffect;
import edu.kit.kastel.monstergame.model.effect.StatusConditionEffect;
import edu.kit.kastel.monstergame.model.enums.StatusCondition;
import edu.kit.kastel.monstergame.model.enums.DamageType;
import edu.kit.kastel.monstergame.model.enums.EffectTarget;
import edu.kit.kastel.monstergame.model.enums.Element;
import edu.kit.kastel.monstergame.model.enums.StatType;
import edu.kit.kastel.monstergame.model.enums.ProtectionTarget;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;


public class FileParser {
    public static class GameData {
        private List<Monster> monsters;
        private List<Action> actions;
        private Map<String, Action> actionMap;

        public GameData(List<Monster> monsters, List<Action> actions) {
            this.monsters = monsters;
            this.actions = actions;
            this.actionMap = new HashMap<>();
            for (Action action : actions) {
                actionMap.put(action.getName(), action);
            }
        }

        public List<Monster> getMonsters() {
            return monsters;
        }

        public List<Action> getActions() {
            return actions;
        }

        public Action getActionByName(String name) {
            return actionMap.get(name);
        }
    }

    public static GameData parseFile(String filename) {
        Map<String, Action> actionsMap = new HashMap<>();
        List<Monster> monsters = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                if (line.startsWith("action ")) {
                    parseAction(line, reader, actionsMap);
                } else if (line.startsWith("monster ")) {
                    parseMonster(line, actionsMap, monsters);
                }
            }

            List<Action> actions = new ArrayList<>(actionsMap.values());
            return new GameData(monsters, actions);

        } catch (IOException e) {
            System.err.println("Error parsing file: " + e.getMessage());
            return new GameData(new ArrayList<>(), new ArrayList<>());
        }
    }

    private static void parseAction(String firstLine, BufferedReader reader, Map<String, Action> actionsMap) throws IOException {
        String[] parts = firstLine.split("\\s+", 3);
        if (parts.length < 3) {
            System.err.println("Invalid action format: " + firstLine);
            return;
        }

        String actionName = parts[1];
        Element element;
        element = Element.valueOf(parts[2]);


        List<Effect> effects = new ArrayList<>();
        String line;

        // Read effects until "end action"
        while ((line = reader.readLine()) != null) {
            line = line.trim();

            if (line.equals("end action")) {
                break;
            }

            Effect effect = parseEffect(line);
            if (effect != null) {
                effects.add(effect);
            }
        }

        Action action = new Action(actionName, element, effects);
        actionsMap.put(actionName, action);
    }

    private static Effect parseEffect(String line) {
        String[] parts = line.split("\\s+");
        if (parts.length == 0) {
            return null;
        }
        String effectType = parts[0];
        switch (effectType) {
            case "damage":
                if (parts.length < 5) {
                    return null;
                }
                EffectTarget damageTarget = getEffectTarget(parts[1]);
                DamageType damageType;
                if (parts[2].equalsIgnoreCase("base")) {
                    damageType = DamageType.BASE;
                } else if (parts[2].equalsIgnoreCase("relative")) {
                    damageType = DamageType.RELATIVE;
                } else if (parts[2].equalsIgnoreCase("abs")) {
                    damageType = DamageType.ABSOLUTE;
                } else {
                    System.err.println("Unknown damage type: " + parts[2]);
                    return null;
                }
                int power = Integer.parseInt(parts[3]);
                double hitRate = Double.parseDouble(parts[4]) / 100.0; // Convert percentage to decimal
                return new DamageEffect(damageTarget, damageType, power, hitRate);
            case "inflictStatusCondition":
                if (parts.length < 4) {
                    return null;
                }
                EffectTarget statusTarget = getEffectTarget(parts[1]);
                StatusCondition condition;
                condition = StatusCondition.valueOf(parts[2]);
                double statusHitRate = Double.parseDouble(parts[3]) / 100.0;
                return new StatusConditionEffect(statusTarget, condition, statusHitRate);
            case "inflictStatChange":
                if (parts.length < 5) {
                    return null;
                }
                EffectTarget statTarget = getEffectTarget(parts[1]);
                StatType stat;
                stat = StatType.valueOf(parts[2]);
                int stages = Integer.parseInt(parts[3]);
                double statHitRate = Double.parseDouble(parts[4]) / 100.0;
                return new StatChangeEffect(statTarget, stat, stages, statHitRate);
            case "protectStat":
                if (parts.length < 4) {
                    return null;
                }
                ProtectionTarget protectTarget;
                if (parts[1].equalsIgnoreCase("health")) {
                    protectTarget = ProtectionTarget.HEALTH;
                } else if (parts[1].equalsIgnoreCase("stats")) {
                    protectTarget = ProtectionTarget.STATS;
                } else {
                    System.err.println("Unknown protection target: " + parts[1]);
                    return null;
                }
                if (parts[2].equalsIgnoreCase("random") && parts.length >= 6) {
                    int minRounds = Integer.parseInt(parts[3]);
                    int maxRounds = Integer.parseInt(parts[4]);
                    double protectHitRate = Double.parseDouble(parts[5]) / 100.0;
                    return new ProtectionEffect(protectTarget, minRounds, maxRounds, protectHitRate);
                } else {
                    int rounds = Integer.parseInt(parts[2]);
                    double protectHitRate = Double.parseDouble(parts[3]) / 100.0;
                    return new ProtectionEffect(protectTarget, rounds, protectHitRate);
                }
            case "heal":
                if (parts.length < 5) {
                    return null;
                }
                EffectTarget healTarget = getEffectTarget(parts[1]);
                DamageType healType;
                if (parts[2].equalsIgnoreCase("base")) {
                    healType = DamageType.BASE;
                } else if (parts[2].equalsIgnoreCase("relative")) {
                    healType = DamageType.RELATIVE;
                } else if (parts[2].equalsIgnoreCase("abs")) {
                    healType = DamageType.ABSOLUTE;
                } else {
                    System.err.println("Unknown heal type: " + parts[2]);
                    return null;
                }

                int healPower = Integer.parseInt(parts[3]);
                double healHitRate = Double.parseDouble(parts[4]) / 100.0;

            default:
                System.err.println("Unknown effect type: " + effectType);
                return null;
        }
    }

    private static EffectTarget getEffectTarget(String targetStr) {
        if (targetStr.equalsIgnoreCase("target")) {
            return EffectTarget.TARGET;
        } else if (targetStr.equalsIgnoreCase("user") || targetStr.equalsIgnoreCase("self")) {
            return EffectTarget.SELF;
        } else {
            System.err.println("Unknown target: " + targetStr);
            return EffectTarget.TARGET; // Default
        }
    }

    private static void parseMonster(String line, Map<String, Action> actionsMap, List<Monster> monsters) {
        String[] parts = line.split("\\s+");
        if (parts.length < 7) {
            System.err.println("Invalid monster format: " + line);
            return;
        }

        String monsterName = parts[1];
        Element element;
        element = Element.valueOf(parts[2]);


        // Parse stats
        Map<StatType, Integer> stats = new EnumMap<>(StatType.class);
        stats.put(StatType.HP, Integer.parseInt(parts[3]));
        stats.put(StatType.ATK, Integer.parseInt(parts[4]));
        stats.put(StatType.DEF, Integer.parseInt(parts[5]));
        stats.put(StatType.SPD, Integer.parseInt(parts[6]));
        stats.put(StatType.PRC, 1); // Default values
        stats.put(StatType.AGL, 1); // Default values

        // Parse actions
        List<Action> monsterActions = new ArrayList<>();
        for (int i = 7; i < parts.length; i++) {
            String actionName = parts[i];
            Action action = actionsMap.get(actionName);
            if (action != null) {
                monsterActions.add(action);
            } else {
                System.err.println("Unknown action for monster " + monsterName + ": " + actionName);
            }
        }

        Monster monster = new Monster(monsterName, element, stats, monsterActions);
        monsters.add(monster);
    }
}