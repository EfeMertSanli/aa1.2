package edu.kit.kastel.monstergame.model.util;
import edu.kit.kastel.monstergame.model.Monster;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Utility methods for combat operations.
 * @author uuifx
 */
public final class CombatUtil {

    /**
     * Private constructor to prevent instantiation.
     */
    private CombatUtil() {
    }

    /**
     * Sorts monsters by contestant number.
     *
     * @param monsters The list of monsters to sort
     * @return A new list with monsters sorted by contestant number
     */
    public static List<Monster> sortByContestantNumber(List<Monster> monsters) {
        List<Monster> sortedMonsters = new ArrayList<>(monsters);
        Collections.sort(sortedMonsters, new Comparator<Monster>() {
            @Override
            public int compare(Monster m1, Monster m2) {
                return Integer.compare(m1.getContestantNumber(), m2.getContestantNumber());
            }
        });
        return sortedMonsters;
    }

    /**
     * Sorts monsters by effective speed (higher speeds first).
     *
     * @param monsters The list of monsters to sort
     * @return A new list with monsters sorted by effective speed
     */
    public static List<Monster> sortBySpeed(List<Monster> monsters) {
        List<Monster> sortedMonsters = new ArrayList<>(monsters);
        Collections.sort(sortedMonsters, (m1, m2) ->
                Integer.compare(m2.getEffectiveSpeed(), m1.getEffectiveSpeed()));
        return sortedMonsters;
    }

    /**
     * Gets active monsters from a list.
     *
     * @param monsters The list of monsters to filter
     * @return A new list containing only non-defeated monsters
     */
    public static List<Monster> getActiveMonsters(List<Monster> monsters) {
        List<Monster> activeMonsters = new ArrayList<>();
        for (Monster monster : monsters) {
            if (!monster.isDefeated()) {
                activeMonsters.add(monster);
            }
        }
        return activeMonsters;
    }

    /**
     * Finds a monster in a list by contestant number.
     *
     * @param monsters The list of monsters to search
     * @param contestantNumber The contestant number to find
     * @return The monster with the specified contestant number, or null if not found
     */
    public static Monster findMonsterByContestantNumber(List<Monster> monsters, int contestantNumber) {
        for (Monster monster : monsters) {
            if (monster.getContestantNumber() == contestantNumber) {
                return monster;
            }
        }
        return null;
    }
}