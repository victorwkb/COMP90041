/*
 * Student name: Kai Bing Goh
 * Student ID: 981436
 * LMS username: kaibingg
 */

import java.util.*;

/**
 * Child Class of Competition class, created when competition this mode is selected
 */
public class RandomPickCompetition extends Competition {
    // Constants for the won prizes
    private final int FIRST_PRIZE = 50000;
    private final int SECOND_PRIZE = 5000;
    private final int THIRD_PRIZE = 1000;
    private final int[] prizes = {FIRST_PRIZE, SECOND_PRIZE, THIRD_PRIZE};
    private final int MAX_WINNING_ENTRIES = 3;

    /**
     * Method to addEntries to the RandomPickCompetition
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
                billId = keyboard.next();
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
                // A valid input now, can check with list of bills if it exists, used or lack info
                else if (!data.billExists(billId)) {
                    System.out.println("This bill does not exist. Please try again.");
                } else if (data.billExists(billId) && data.billNoMemId(billId)) {
                    System.out.println("This bill has no member id. Please try again.");
                } else {
                    // bill exist for sure at this point so we can get the bill
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

            // Calculate amount of entries and prints out the entire list of entries
            double amount = Double.parseDouble(currentBill.getAmount());
            int numEntry = (int) (amount / 50);
            System.out.println("This bill ($" + amount + ") is eligible for " + numEntry +
                    " entries.");
            System.out.println("The following entries have been automatically generated:");
            for (int i = 0; i < numEntry; i++) {
                updateEntryCounter();
                Entry currentEntry = new Entry();
                currentEntry.setEntryId(getEntryCounter());
                currentEntry.setMemId(currentBill.getMemIdNum());
                currentEntry.setName(currentMember.getName());
                addToEntryList(currentEntry);
                System.out.printf("Entry ID: %-6d\n", getEntryCounter());
            }
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
                    break ynCheckLoop;
                } else if (yesOrNo.equalsIgnoreCase("n")) {
                    break entryLoop;
                }
            }
        }
    }

    /**
     * RandomPick drawWinners using CompID and award prizes to picked winners
     *
     * @param sc SimpleCompetitions containing all the information
     */
    public void drawWinners(SimpleCompetitions sc) {
        if (getEntryCounter() < 3) {
            System.out.println("Need at least 3 entries, please add more entries!");
            return;
        }
        Random randomGenerator = null;
        if (this.getTestingMode()) {
            randomGenerator = new Random(this.getCompId());
        } else {
            randomGenerator = new Random();
        }

        Set<Integer> memWonList = new HashSet<Integer>();
        ArrayList<Entry> winningList = new ArrayList<Entry>();
        int winningEntryCount = 0;
        while (winningEntryCount < MAX_WINNING_ENTRIES) {
            // This loop ensures that selected entries with prize will not be selected since it
            // does nothing and goes to the next iteration
            while (true) {
                int winningEntryIndex = randomGenerator.nextInt(getEntryList().size());
                Entry winningEntry = getEntry(winningEntryIndex);
                if (winningEntry.getPrizeWon() != 0) {
                } else if (winningEntry.getPrizeWon() == 0) {
                    int currentPrize = prizes[winningEntryCount];
                    winningEntry.setPrizeWon(currentPrize);
                    winningEntryCount++;
                    winningList.add(winningEntry);
                    memWonList.add(Integer.parseInt(winningEntry.getMemId()));
                    break;
                }
            }
        }
        System.out.printf("Competition ID:%2d, Competition Name: %s, Type: RandomPickCompetition\n"
                , getCompId(), getCompName());
        System.out.println("Winning entries:");
        // using that list to iterate through winning list to find max prize
        int max = 0;
        ArrayList<Entry> finalList = new ArrayList<Entry>();
        Entry track = new Entry();
        for (int id : memWonList) {
            // Second iteration through the entryList
            for (Entry check2 : getEntryList()) {
                if ((Integer.parseInt(check2.getMemId()) == id) && (check2.getPrizeWon() > max)) {
                    max = check2.getPrizeWon();
                    track = check2;
                }
            }
            max = 0;
            finalList.add(track);
        }
        // Built in ArrayList sorter according to entryId
        finalList.sort(Comparator.comparing(Entry::getEntryId));
        for (Entry print : finalList) {
            // Assign a variable to track total prizes won (from the final list)
            addAwardedPrizes(print.getPrizeWon());
            // Prints differently depending on the mode
            System.out.printf("Member ID: %6s, Member Name: %s, Entry ID: %d, Prize: %-5d\n",
                    print.getMemId(), print.getName(), print.getEntryId(), print.getPrizeWon());
            updateNumWonEntries();
        }
    }
}