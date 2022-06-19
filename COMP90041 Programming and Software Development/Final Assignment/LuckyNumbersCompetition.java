/*
 * Student name: Kai Bing Goh
 * Student ID: 981436
 * LMS username: kaibingg
 */

import java.util.*;

/**
 * Child Class of Competition class, created when competition this mode is selected
 */
public class LuckyNumbersCompetition extends Competition {
    // Constants for prizesWon
    private final int DIVISION_ONE_PRIZE = 50000;
    private final int DIVISION_TWO_PRIZE = 5000;
    private final int DIVISION_THREE_PRIZE = 1000;
    private final int DIVISION_FOUR_PRIZE = 500;
    private final int DIVISION_FIVE_PRIZE = 100;
    private final int DIVISION_SIX_PRIZE = 50;

    /**
     * Method to addEntries to the LuckyNumbersCompetition
     *
     * @param keyboard Scanner class to input
     * @param data     DataProvider class holds all Bill and Member information from files input
     * @param sc       SimpleCompetitions containing all the information
     */
    public void addEntries(Scanner keyboard, DataProvider data, SimpleCompetitions sc) {
        Bill currentBill;
        Member currentMember;
        String billId;
        entryLoop:
        while (true) {
            while (true) {
                System.out.println("Bill ID: ");
                billId = keyboard.nextLine();
                // Checks if the entered (line) is 6 digits only
                boolean flag = true;
                for (int i = 0; i < billId.length(); i++) {
                    if (Character.isDigit(billId.charAt(i)) == flag) {
                    } else {
                        flag = false;
                        break;
                    }
                }
                if ((billId.length() != 6) || (!flag)) {
                    System.out.println("Invalid bill id! It must be a 6-digit number. " +
                            "Please try again.");
                }
                // A valid input now, so can check bill's existence, lack info or used
                else if (!data.billExists(billId)) {
                    System.out.println("This bill does not exist. Please try again.");
                } else if (data.billExists(billId) && data.billNoMemId(billId)) {
                    System.out.println("This bill has no member id. Please try again.");
                } else {
                    // bill valid for sure at this point so we can get the bill
                    currentBill = data.getBill(billId);
                    currentMember = data.getMember(currentBill.getMemIdNum());
                    break;
                }
            }
            // Check for bill availability, if used ask for bill id again
            if (currentBill.getBillStatus()) {
                System.out.println("This bill has already been used " +
                        "for a competition. Please try again.");
                continue;
            } else {
                // Bill will be used, change bill availability status
                currentBill.setBillStatus(true);
            }
            // calculate number of entries available
            double amount = Double.parseDouble(currentBill.getAmount());
            int numEntry = (int) (amount / 50);
            int numManual = validManual(amount, keyboard, numEntry);
            // Clears the previous line (numManual ends on that line)
            keyboard.nextLine();
            // Store entries while incrementing seed variable
            for (int i = 0; i < numManual; i++) {
                NumbersEntry currentEntry = new NumbersEntry();
                currentEntry.setManualNumbers(keyboard, sc);
                currentEntry.setAutoStatus(false);
                updateEntryCounter();
                currentEntry.setEntryId(getEntryCounter());
                currentEntry.setMemId(currentBill.getMemIdNum());
                currentEntry.setName(currentMember.getName());
                addToEntryList(currentEntry);
            }
            for (int i = 0; i < (numEntry - numManual); i++) {
                AutoNumbersEntry currentAutoEntry = new AutoNumbersEntry();
                if (getTestingMode()) {
                    currentAutoEntry.setNumbers(currentAutoEntry.createNumbers(sc.getSeed()));
                } else {
                    currentAutoEntry.setNumbers(currentAutoEntry.createNumbers());
                }
                currentAutoEntry.setAutoStatus(true);
                updateEntryCounter();
                currentAutoEntry.setEntryId(getEntryCounter());
                currentAutoEntry.setMemId(currentBill.getMemIdNum());
                currentAutoEntry.setName(currentMember.getName());
                sc.updateSeed();
                addToEntryList(currentAutoEntry);
            }
            printEntries(numEntry);
            // Ask for more entries, repeat starting loop if yes,
            // otherwise break outermost loop and end method
            ynCheckLoop:
            while (true) {
                System.out.println("Add more entries (Y/N)?");
                String yesOrNo = keyboard.next();
                if ((!yesOrNo.equalsIgnoreCase("y")) &&
                        (!yesOrNo.equalsIgnoreCase("n"))) {
                    System.out.println("Unsupported option. Please try again!");
                } else if (yesOrNo.equalsIgnoreCase("y")) {
                    keyboard.nextLine();
                    break ynCheckLoop;
                } else if (yesOrNo.equalsIgnoreCase("n")) {
                    break entryLoop;
                }
            }
        }
    }

    /**
     * LuckyNumbers drawWinners, generate a winning entry using seed and award prizes to matches
     *
     * @param sc SimpleCompetitions containing all the information
     */
    public void drawWinners(SimpleCompetitions sc) {
        System.out.printf("Competition ID:%2d, Competition Name: %s, " +
                "Type: LuckyNumbersCompetition\n", getCompId(), getCompName());
        AutoNumbersEntry winningEntry = new AutoNumbersEntry();
        if (getTestingMode()) {
            winningEntry.setNumbers(winningEntry.createNumbers(getCompId()));
            // Seed updated manually after using createNumbers
            sc.updateSeed();
        } else {
            // Normal mode, do not need seed for createNumbers
            winningEntry.setNumbers(winningEntry.createNumbers());
        }
        // Getting numbers array to be printed out
        int[] arr = winningEntry.getNumbers();
        System.out.printf("Lucky Numbers:%3d%3d%3d%3d%3d%3d%3d [Auto]\n",
                arr[0], arr[1], arr[2], arr[3], arr[4], arr[5], arr[6]);
        // Matching lucky entry's number and count number of matches for every entry
        System.out.println("Winning entries:");
        // Using Set to collect unique memID's and add them to a list
        Set<Integer> wonList = new HashSet<Integer>();
        for (Entry index : getEntryList()) {
            int match = 0;
            int[] arr1 = index.getNumbers();
            // Find number of matches for that particular entry
            for (int i = 0; i < arr1.length; i++) {
                for (int j = 0; j < arr1.length; j++) {
                    if (arr1[i] == arr[j]) {
                        match++;
                    }
                }
            }
//          Assign prize won to variables of that Entry according to number of matches
            switch (match) {
                case 2:
                    index.setPrizeWon(DIVISION_SIX_PRIZE);
                    break;
                case 3:
                    index.setPrizeWon(DIVISION_FIVE_PRIZE);
                    break;
                case 4:
                    index.setPrizeWon(DIVISION_FOUR_PRIZE);
                    break;
                case 5:
                    index.setPrizeWon(DIVISION_THREE_PRIZE);
                    break;
                case 6:
                    index.setPrizeWon(DIVISION_TWO_PRIZE);
                    break;
                case 7:
                    index.setPrizeWon(DIVISION_ONE_PRIZE);
                    break;
            }
            // Only add to list if it has more than 1 matches (won prize) with the lucky entry
            if (match > 1) {
                wonList.add(Integer.parseInt(index.getMemId()));
            }
        }
        // Find max prize won among the list of memID's, wonList only keeps memID's with won
        // prizes so different prizes won from different entries but same memID can be compared
        // to keep the highest winning prize for that particular member ID
        int max = 0;
        ArrayList<Entry> winningList = new ArrayList<Entry>();
        Entry track = new Entry();
        for (int id : wonList) {
            // Second iteration through the entryList
            for (Entry entry : getEntryList()) {
                if ((Integer.parseInt(entry.getMemId()) == id) && (entry.getPrizeWon() > max)) {
                    max = entry.getPrizeWon();
                    track = entry;
                }
            }
            max = 0;
            winningList.add(track);
        }
        // Sort the ArrayList using built in function in ascending order of entry ID
        winningList.sort(Comparator.comparing(Entry::getEntryId));
        // winningList only contains memID's with their highest won prizes
        printWinList(winningList);
    }

    /**
     * Method to print the won entries while updating no. of won entries and awarded prizes
     *
     * @param winningList List of won entries from the lucky draw
     */
    public void printWinList(ArrayList<Entry> winningList) {
        for (Entry print : winningList) {
            // Assign a variable to track total prizes won (from the final list)
            addAwardedPrizes(print.getPrizeWon());
            int[] arr2 = print.getNumbers();
            // Prints differently depending on if it is automatically generated
            System.out.printf("Member ID: %6s, Member Name: %s, Prize: %-5d\n",
                    print.getMemId(), print.getName(), print.getPrizeWon());
            if (!print.getAutoStatus()) {
                System.out.printf("--> Entry ID: %d, Numbers:%3d%3d%3d%3d%3d%3d%3d\n",
                        print.getEntryId(), arr2[0], arr2[1], arr2[2], arr2[3],
                        arr2[4], arr2[5], arr2[6]);
            }
            if (print.getAutoStatus()) {
                System.out.printf("--> Entry ID: %d, Numbers:%3d%3d%3d%3d%3d%3d%3d [Auto]\n",
                        print.getEntryId(), arr2[0], arr2[1], arr2[2], arr2[3],
                        arr2[4], arr2[5], arr2[6]);
            }
            updateNumWonEntries();
        }
    }

    /**
     * Method to print all entries
     *
     * @param numEntry number of total entries to be printed out
     */
    public void printEntries(int numEntry) {
        // Prints out the entire list of entries
        if (numEntry != 0) {
            System.out.println("The following entries have been added:");
        }
        for (int i = 0; i < numEntry; i++) {
            int[] arr;
            Entry printEntry = getEntry(getCount());
            arr = printEntry.getNumbers();
            // If autoStatus is 0 then it means it is not auto generated, they print differently
            if (!printEntry.getAutoStatus()) {
                System.out.printf("Entry ID: %-2d     Numbers:%3d%3d%3d%3d%3d%3d%3d\n",
                        printEntry.getEntryId(), arr[0], arr[1],
                        arr[2], arr[3], arr[4], arr[5], arr[6]);
            } else {
                System.out.printf("Entry ID: %-2d     Numbers:%3d%3d%3d%3d%3d%3d%3d [Auto]\n",
                        printEntry.getEntryId(), arr[0], arr[1],
                        arr[2], arr[3], arr[4], arr[5], arr[6]);
            }
            updateCount();
        }
    }

    /**
     * Method to ask and check if inputted number of manual entry is valid
     *
     * @param amount   Amount of the bill
     * @param keyboard Scanner class to input
     * @param numEntry Total number of entry
     * @return Returns a valid number of manual entries' request
     */
    public int validManual(double amount, Scanner keyboard, int numEntry) {
        System.out.println("This bill ($" + amount + ") is eligible for " + numEntry +
                " entries. How many manual entries did the customer fill up?: ");
        // ask for number of manual entries (the rest auto generated)
        int numManual;
        while (true) {
            // ensure correct input type
            try {
                numManual = keyboard.nextInt();
            } catch (InputMismatchException e) {
                continue;
            }
            if (numManual > numEntry) {
                System.out.println("The number must be in the range from 0 to "
                        + numEntry + ". Please try again.");
            } else {
                break;
            }
        }
        return numManual;
    }
}
