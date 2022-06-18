/*
 * Student name: Kai Bing Goh
 * Student ID: 981436
 * LMS username: kaibingg
 */

import java.io.Serializable;

/**
 * Class that stores information regarding any entries, parent class of NumbersEntry and
 * AutoNumbersEntry
 */
public class Entry implements Serializable {
    private String memId;
    private String name;
    private String billId;
    private int entryId;
    private int prizeWon = 0;
    private int[] numbers;
    private boolean autoStatus;

    // List of setter and getter methods

    public void setPrizeWon(int anAmount) {
        prizeWon = anAmount;
    }

    public int getPrizeWon() {
        return prizeWon;
    }

    public void setEntryId(int anEntryId) {
        entryId = anEntryId;
    }

    public int getEntryId() {
        return entryId;
    }

    public void setMemId(String aMemId) {
        memId = aMemId;
    }

    public String getMemId() {
        return memId;
    }

    public void setBillId(String aBillId) {
        billId = aBillId;
    }

    public String getBillId() {
        return billId;
    }

    public void setAutoStatus(boolean aStatus) {
        autoStatus = aStatus;
    }

    public boolean getAutoStatus() {
        return autoStatus;
    }

    public void setNumbers(int[] numbers) {
        this.numbers = numbers;
    }

    public int[] getNumbers() {
        return numbers;
    }

    public void setName(String aName) {
        name = aName;
    }

    public String getName() {
        return name;
    }
}
