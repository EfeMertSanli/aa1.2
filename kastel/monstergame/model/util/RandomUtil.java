package edu.kit.kastel.monstergame.model.util;


import java.util.Random;
import java.util.Scanner;

/**
 * Utility class for generating random numbers.
 * @author uuifx
 */
public class RandomUtil {
    private final Random random;
    private final boolean debugMode;
    private final Scanner scanner;

    /**
     * Creates a new RandomUtil instance.
     *
     * @param seed The seed for the random number generator (or -1 for no seed)
     * @param debugMode Whether to run in debug mode with interactive prompts
     * @author uuifx
     */
    public RandomUtil(long seed, boolean debugMode) {
        if (seed != -1) {
            this.random = new Random(seed);
        } else {
            this.random = new Random();
        }

        this.debugMode = debugMode;
        this.scanner = debugMode ? new Scanner(System.in) : null;
    }

    /**
     * Generates a boolean value with the given probability.
     * Used for critical hits, hit rates, and status condition endings.
     *
     * @param probability The probability of returning true
     * @param decisionDescription Description for debugmode
     * @return true with the given probability
     * @author uuifx
     */
    public boolean rollChance(double probability, String decisionDescription) {
        if (debugMode) {
            System.out.printf("Decide %s: yes or no (y/n)? ", decisionDescription);
            String input = scanner.nextLine().trim().toLowerCase();
            return input.equals("y") || input.equals("yes");
        } else {
            return random.nextDouble() * 100 <= probability;
        }
    }

    /**
     * Generates a random double in the range.
     * Used for damage calculations with random
     * @param min The minimum value
     * @param max The maximum value
     * @param decisionDescription Description for debugmode
     * @return A random double in the range
     * @author uuifx
     */
    public double getRandomDouble(double min, double max, String decisionDescription) {
        if (debugMode) {
            System.out.printf("Decide %s: a double between %.2f and %.2f? ",
                    decisionDescription, min, max);
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Using default value.");
                return min + (max - min) / 2; // Default to middle of range on error
            }
        } else {
            return random.nextDouble(min, max);
        }
    }

    /**
     * Generates a random integer in the range.
     * Used for random repeat counts and protection
     * @param min The minimum value
     * @param max The maximum value
     * @param decisionDescription Description for debugmode
     * @return A random integer in the range
     * @author uuifx
     */
    public int getRandomInt(int min, int max, String decisionDescription) {
        if (debugMode) {
            System.out.printf("Decide %s: an integer between %d and %d? ",
                    decisionDescription, min, max);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Using default value.");
                return min + (max - min) / 2; // Default to middle of range on error
            }
        } else {
            return random.nextInt(min, max + 1);
        }
    }
}