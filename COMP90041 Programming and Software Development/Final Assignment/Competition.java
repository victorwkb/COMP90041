/*
 * Student name: Kai Bing Goh
 * Student ID: 981436
 * LMS username: kaibingg
 */

import java.io.Serializable;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * Abstract class that stores a competition's information including entries, parent class of child
 * classes LuckyNumbersCompetition and RandomPickCompetition
 */
public abstract class Competition implements Serializable {
    private String name; //competition name
    private int id; //competition identifier
    private boolean testingMode; // true for testing mode, false for normal mode
    private boolean luckyMode; // true for lucky mode, false for random mode
    private boolean compStatus = true; // true for active, false for completed/inactive
    private final ArrayList<Entry> entryList = new ArrayList<Entry>();
    private int entryCounter = 0;
    private int count = 0;
    private int awardedPrizes = 0;
    private int numOfWonEntries = 0;

    //  Some setters and getters for the private instances

    public void setCompName(String aName) {
        name = aName;
    }

    public String getCompName() {
        return name;
    }

    public void setCompId(int anId) {
        id = anId;
    }

    public int getCompId() {
        return id;
    }

    public void setTestingMode(boolean aMode) {
        testingMode = aMode;
    }

    public boolean getTestingMode() {
        return testingMode;
    }

    /**
     * set competition type (LuckyNumbers or RandomPick) via a string
     *
     * @param luckyOrRandom A string L/R case insensitive to update competition's private L/R mode
     */
    public void isLuckyMode(String luckyOrRandom) {
        if (luckyOrRandom.equalsIgnoreCase("L")) {
            luckyMode = true;
        } else if (luckyOrRandom.equalsIgnoreCase("R")) {
            luckyMode = false;
        }
    }

    public boolean getIsLuckyMode() {
        return luckyMode;
    }

    public void setCompStatus(boolean aCompStatus) {
        compStatus = aCompStatus;
    }

    public boolean getCompStatus() {
        return compStatus;
    }

    // Some methods for accessing private arraylist in child classes

    public Entry getEntry(int index) {
        return entryList.get(index);
    }

    public ArrayList<Entry> getEntryList() {
        return entryList;
    }

    public void addToEntryList(Entry anEntry) {
        entryList.add(anEntry);
    }

    // some methods to increment private counter variables

    public void updateEntryCounter() {
        entryCounter++;
    }

    public int getEntryCounter() {
        return entryCounter;
    }

    public void updateCount() {
        count++;
    }

    public int getCount() {
        return count;
    }

    public void updateNumWonEntries() {
        numOfWonEntries++;
    }

    public int getNumOfWonEntries() {
        return numOfWonEntries;
    }

    // Methods to update and get private variables for summary

    public void addAwardedPrizes(int anAwardedPrizes) {
        awardedPrizes += anAwardedPrizes;
    }

    public int getAwardedPrizes() {
        return awardedPrizes;
    }

    /**
     * Overridden (by child classes) abstract addEntries method declarations
     *
     * @param keyboard Scanner class to input
     * @param data     DataProvider class has all Bill and Member information from files input
     * @param sc       SimpleCompetitions containing all the information
     */
    public abstract void addEntries(Scanner keyboard, DataProvider data, SimpleCompetitions sc);

    /**
     * Overridden (by child classes) abstract drawWinners method declarations
     *
     * @param sc SimpleCompetitions containing all the information
     */
    public abstract void drawWinners(SimpleCompetitions sc);
}
