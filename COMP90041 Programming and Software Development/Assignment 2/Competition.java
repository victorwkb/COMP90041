// Your full name: Kai Bing Goh
// Your username (from Canvas, e.g., yourname@student.unimelb.edu.au): 
//      kaibingg@student.unimelb.edu.au
// Github repository link: 
//      https://github.com/COMP90041/sem1-2021-assignment-2-victorwkb
// By submitting work for assessment I hereby declare that I understand the University’s policy 
// on academic integrity and that the work submitted is original and solely my work, and that I 
// have not been assisted by any other person (collusion) apart from where the submitted work is 
// for a designated collaborative task, in which case the individual contributions are indicated. 
// I also declare that I have not used any sources without proper acknowledgment (plagiarism). 
// Where the submitted work is a computer program or code, I further declare that any copied code 
// is declared in comments identifying the source at the start of the program or in a header file, 
// that comments inline identify the start and end of the copied code, and that any modifications 
// to code sources elsewhere are commented upon as to the nature of the modification.

import java.util.HashSet;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Set;

public class Competition {
    private String name;
    private int id;
    public boolean compStatus = true;
    public ArrayList<Entry> entryList = new ArrayList<Entry>();
    public AutoEntry ae = new AutoEntry();
    public int entryCounter = 0;
    public int count = 0;
    public int awardedPrizes = 0;
    public int numOfWonEntries = 0;

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

    //Method to add entries to the competition
    public void addEntries(Scanner keyboard, String t_n) {
        Entry newEntry = new Entry();
        entryloop:
        while (true) {
//          Initialised new entry and proceed to collect member ID and bill ID
            while (true) {
                System.out.println("Member ID: ");
                String memId = keyboard.next();
//              Checks if input string has correct format (only digits and correct length)
                boolean flag = true;
                for (int i = 0; i < memId.length(); i++) {
                    if (Character.isDigit(memId.charAt(i)) == flag) {
                    } else {
                        flag = false;
                        break;
                    }
                }
                if ((memId.length() != 6) || (!flag)) {
                    System.out.println("Invalid member id! It must be a 6-digit number. " +
                            "Please try again.");
                } else {
//                  if valid then convert the string to int and store/set the entry's member Id
                    newEntry.setMemId(Integer.parseInt(memId));
                    break;
                }
            }
//          Repeat for bill ID
            while (true) {
                System.out.println("Bill ID: ");
                String billId = keyboard.next();
                boolean flag2 = true;
                for (int i = 0; i < billId.length(); i++) {
                    if (Character.isDigit(billId.charAt(i)) == flag2) {
                    } else {
                        flag2 = false;
                        break;
                    }
                }
                if ((billId.length() != 6) || (!flag2)) {
                    System.out.println("Invalid bill id! It must be a 6-digit number. " +
                            "Please try again.");
                } else {
//                  if valid then convert the string to int and store/set the entry's bill Id
                    newEntry.setBillId(Integer.parseInt(billId));
                    break;
                }
            }

//          Check for number of entries by asking for the amount
            System.out.println("Total amount: ");
            double amount = keyboard.nextDouble();
            int numEntry = (int) (amount / 50);
//          Prompts warnings for invalid inputs
            if (numEntry == 0) {
                System.out.println("This bill is not eligible for an entry. " +
                        "The total amount is smaller than $50.0");
            } else {
                System.out.println("This bill is eligible for " + numEntry +
                        " entries. How many manual entries did the customer fill up?: ");
//              Set manual and auto numbers into entry list (print entry list later)
//              Prompt for number of manual entries to be inserted
                int numManual = keyboard.nextInt();
//              Clears the previous line (numManual ends on that line)
                keyboard.nextLine();
//              Store entries while incrementing statically stored seed variable
                for (int i = 0; i < numManual; i++) {
                    Entry currentEntry = new Entry();
//                  Seed for every new entry is updated in setManualNumbers()
                    newEntry.setManualNumbers(keyboard);
//                  autoStatus shows if that entry is automatically generated
                    newEntry.autoStatus = 0;
                    entryCounter++;
//                  Set entry id after entryCounter + 1 since entryCounter's index starts at 0
                    newEntry.setEntryId(entryCounter);
//                  currentEntry temporarily copies everything in the entry and added to the list
//                  int[] numbers of newEntry is constantly updated even if billID/memID unchanged
//                  and for every new int[] numbers array (entries)
                    currentEntry = newEntry.getEntry();
                    entryList.add(currentEntry);
                }
//              Similarly for remaining entries, automate it using createNumbers, depends on mode
                for (int i = 0; i < (numEntry - numManual); i++) {
                    Entry currentEntry = new Entry();
                    if (t_n.equalsIgnoreCase("t")) {
                        newEntry.setNumbers(ae.createNumbers(SimpleCompetitions.seed));
                    } else if (t_n.equalsIgnoreCase("n")) {
                        newEntry.setNumbers(ae.createNumbers());
                    }
//                  Uses seed for test mode, autoStatus assigned 1 shows its auto-generated
                    newEntry.autoStatus = 1;
                    entryCounter++;
                    newEntry.setEntryId(entryCounter);
//                  Seed updated manually after using createNumbers
                    SimpleCompetitions.seed++;
                    currentEntry = newEntry.getEntry();
                    entryList.add(currentEntry);
                }
            }
//          Prints out the entire list of entries
            if (numEntry != 0) {
                System.out.println("The following entries have been added:");
            }
            for (int i = 0; i < numEntry; i++) {
                int[] arr = new int[7];
                Entry printEntry = entryList.get(count);
                int[] entArray = printEntry.getNumbers();
                System.arraycopy(entArray, 0, arr, 0, 7);
//              If autoStatus is 0 then it means it is not auto generated, they print differently
                if (printEntry.autoStatus == 0) {
                    System.out.printf("Entry ID: %-2d     Numbers:%3d%3d%3d%3d%3d%3d%3d\n",
                            printEntry.getEntryId(), arr[0], arr[1],
                            arr[2], arr[3], arr[4], arr[5], arr[6]);
                } else {
                    System.out.printf("Entry ID: %-2d     Numbers:%3d%3d%3d%3d%3d%3d%3d [Auto]\n",
                            printEntry.getEntryId(), arr[0], arr[1],
                            arr[2], arr[3], arr[4], arr[5], arr[6]);
                }
                count++;
            }
//          Ask for more entries, repeat starting loop if yes,
//          otherwise break outermost loop and end method
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
                    break entryloop;
                }
            }
        }
    }

    //  Method to find winning entries
    public void drawWinners(String t_n) {
        System.out.printf("Lucky entry for Competition ID:%2d, Competition Name: %s\n", id, name);
        Entry winningEntry = new Entry();
        if (t_n.equalsIgnoreCase("t")) {
            winningEntry.setNumbers(ae.createNumbers(getCompId()));
//          Seed updated manually after using createNumbers
            SimpleCompetitions.seed++;
        } else if (t_n.equalsIgnoreCase("n")) {
            winningEntry.setNumbers(ae.createNumbers());
        }
//      Copying ArrayList into array to be printed out
        int[] arr = new int[7];
        int[] entArray = winningEntry.getNumbers();
        System.arraycopy(entArray, 0, arr, 0, 7);
        System.out.printf("Numbers:%3d%3d%3d%3d%3d%3d%3d [Auto]\n",
                arr[0], arr[1], arr[2], arr[3], arr[4], arr[5], arr[6]);
//      Matching lucky entry's number and count number of matches for every entry
        System.out.println("Winning entries:");
        int checkCount = 0;
//      Using Set to collect unique memID's and add them to a list
        Set<Integer> memWonList = new HashSet<Integer>();
        for (Entry check : entryList) {
            int match = 0;
            int[] arr1 = check.getNumbers();
            for (int i = 0; i < arr1.length; i++) {
                for (int j = 0; j < arr1.length; j++) {
                    if (arr1[i] == entArray[j]) {
                        match++;
                    }
                }
            }
//          Assign prize won to variables of that Entry according to number of matches
            switch (match) {
                case 2:
                    check.prizeWon = 50;
                    break;
                case 3:
                    check.prizeWon = 100;
                    break;
                case 4:
                    check.prizeWon = 500;
                    break;
                case 5:
                    check.prizeWon = 1000;
                    break;
                case 6:
                    check.prizeWon = 5000;
                    break;
                case 7:
                    check.prizeWon = 50000;
                    break;
            }
//          Only add it to the list if it has more than 1 matches (won prize) with the lucky entry
            if (match >= 2) {
                memWonList.add(check.getMemId());
            }
        }
//      Find max prize won among the list of memID's, memWonList only keeps memID's with won
//      prizes so different prizes won from different entries but same memID can be compared
        int max = 0;
        ArrayList<Entry> trackList = new ArrayList<Entry>();
        Entry track = new Entry();
        for (int id : memWonList) {
//          Second iteration through the entryList
            for (Entry check2 : entryList) {
                if ((check2.getMemId() == id) && (check2.prizeWon > max)) {
                    max = check2.prizeWon;
                    track = check2;
                }
            }
            max = 0;
            trackList.add(track);
        }
//      trackList only contains memID's with their highest won prizes
        for (Entry print : trackList) {
//          Assign a variable to track total prizes won (from the final list)
            int[] arr2 = print.getNumbers();
            awardedPrizes += print.prizeWon;
//          Prints differently depending on the mode
            if (print.autoStatus == 0) {
                System.out.printf("Member ID: %6d, Entry ID: %-2d    , ",
                        print.getMemId(), print.getEntryId());
                System.out.printf("Prize: %-5d, Numbers:%3d%3d%3d%3d%3d%3d%3d\n",
                        print.prizeWon, arr2[0], arr2[1], arr2[2], arr2[3],
                        arr2[4], arr2[5], arr2[6]);
            }
            if (print.autoStatus == 1) {
                System.out.printf("Member ID: %6d, Entry ID: %-2d    , ",
                        print.getMemId(), print.getEntryId());
                System.out.printf("Prize: %-5d, Numbers:%3d%3d%3d%3d%3d%3d%3d [Auto]\n",
                        print.prizeWon, arr2[0], arr2[1], arr2[2], arr2[3],
                        arr2[4], arr2[5], arr2[6]);
            }
            numOfWonEntries++;
        }
    }

    //  Method to generate report for the user using same instance variable of SimpleCompetitions
    public void report(Scanner keyboard, SimpleCompetitions sc) {
        int completedComp = 0, activeComp = 0;
//      iterate through ArrayList of <Competition> to count completed and active competitions
        for (Competition c : sc.compList) {
            if (!c.compStatus) {
                completedComp++;
            }
        }
        for (Competition c : sc.compList) {
            if (c.compStatus) {
                activeComp++;
            }
        }
        System.out.println("----SUMMARY REPORT----");
        System.out.println("+Number of completed competitions: " + completedComp);
        System.out.println("+Number of active competitions: " + activeComp);
//      Checks the competition's active status and prints out details accordingly by iterating
//      through the ArrayList of type <Competition> that stores all the competitions
        String yOrN;
        for (Competition c : sc.compList) {
            if (c.compStatus) {
                yOrN = "yes";
            } else {
                yOrN = "no";
            }
            System.out.println();
            System.out.printf("Competition ID:%2d, name: %s, active: %s\n",
                    c.getCompId(), c.getCompName(), yOrN);
            System.out.printf("Number of entries: %d\n", c.entryCounter);
            if (!c.compStatus) {
                System.out.printf("Number of winning entries: %d\n", c.numOfWonEntries);
                System.out.printf("Total awarded prizes: %d\n", c.awardedPrizes);
            }
//          total values like numOfWonEntries and awardedPrizes are tracked throughout the code
        }
    }
}