/*
 * Student name: Kai Bing Goh
 * Student ID: 981436
 * LMS username: kaibingg
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * A child class of Entry class where automated entries are generated
 */
public class AutoNumbersEntry extends NumbersEntry {
    private final int NUMBER_COUNT = 7;
    private final int MAX_NUMBER = 35;

    /**
     * Method to create an entry using provided algorithm, used in testing mode
     *
     * @param seed The value of seed used for generating the automated numbers in testing mode
     * @return Returns the generated array of numbers from the seed
     */
    public int[] createNumbers(int seed) {
        ArrayList<Integer> validList = new ArrayList<Integer>();
        int[] tempNumbers = new int[NUMBER_COUNT];
        for (int i = 1; i <= MAX_NUMBER; i++) {
            validList.add(i);
        }
        Collections.shuffle(validList, new Random(seed));
        for (int i = 0; i < NUMBER_COUNT; i++) {
            tempNumbers[i] = validList.get(i);
        }
        Arrays.sort(tempNumbers);
        return tempNumbers;
    }

    /**
     * Method for normal mode (doesn't take any seed, only random)
     *
     * @return Returns the generated array of numbers without using seed
     */
    public int[] createNumbers() {
        ArrayList<Integer> validList = new ArrayList<Integer>();
        int[] tempNumbers = new int[NUMBER_COUNT];
        for (int i = 1; i <= MAX_NUMBER; i++) {
            validList.add(i);
        }
        Collections.shuffle(validList, new Random());
        for (int i = 0; i < NUMBER_COUNT; i++) {
            tempNumbers[i] = validList.get(i);
        }
        Arrays.sort(tempNumbers);
        return tempNumbers;
    }
}
