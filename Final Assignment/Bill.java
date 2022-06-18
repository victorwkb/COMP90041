/*
 * Student name: Kai Bing Goh
 * Student ID: 981436
 * LMS username: kaibingg
 */


/**
 * Class that stores individual bills information, can be updated in the list in DataProvider
 */
public class Bill {
    private String billIdNum;
    private String memIdNum;
    private String amount;
    private boolean billStatus;

    // Some normal setter and getter methods

    public void setBillIdNum(String aBillIdNum) {
        billIdNum = aBillIdNum;
    }

    public String getBillIdNum() {
        return billIdNum;
    }

    public void setMemIdNum(String aMemIdNum) {
        memIdNum = aMemIdNum;
    }

    public String getMemIdNum() {
        return memIdNum;
    }

    public void setAmount(String anAmount) {
        amount = anAmount;
    }

    public String getAmount() {
        return amount;
    }

    // Method that sets a bill's status using a string
    public void setBillStatus(String aStatusString) {
        if (aStatusString.equalsIgnoreCase("true")) {
            billStatus = true;
        } else if (aStatusString.equalsIgnoreCase("false")) {
            billStatus = false;
        }
    }

    public void setBillStatus(boolean aStatus) {
        billStatus = aStatus;
    }

    public boolean getBillStatus() {
        return billStatus;
    }

    // Method throws an exception to be caught in DataProvider when boolean format is wrong
    public void checkStatusFormat(String aStatusString) throws IllegalArgumentException {
        if (aStatusString.equalsIgnoreCase("true")) {
        } else if (aStatusString.equalsIgnoreCase("false")) {
        } else {
            throw new IllegalArgumentException();
        }
    }
}

