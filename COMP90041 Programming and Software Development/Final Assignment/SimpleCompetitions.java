/*
 * Student name: Kai Bing Goh
 * Student ID: 981436
 * LMS username: kaibingg
 */

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Class that stores all competitions' information
 */
public class SimpleCompetitions implements Serializable {
    // Declare objects for later use
    private int seed; // Seed for auto generation of entry/draw numbers
    private boolean activeStatus = false; // Status for if any competition is active
    private boolean testingMode; // True for testing mode, false for normal mode
    private Competition currentComp; // Currently active competition
    private final ArrayList<Competition> compList = new ArrayList<Competition>();

    // Update seed + 1 everytime seed is used
    public void updateSeed() {
        seed++;
    }

    public int getSeed() {
        return seed;
    }

    // Method to set testing mode
    public void isTestingMode(String testOrNo) {
        if (testOrNo.equalsIgnoreCase("T")) {
            testingMode = true;
        } else if (testOrNo.equalsIgnoreCase("N")) {
            testingMode = false;
        }
    }

    /**
     * Method to add new Competition class with inputted competition's information
     *
     * @param keyboard       Scanner class to input
     * @param compCount      A variable in SimpleCompetition to track amount of competitions
     * @param newCompetition The competition class that new information are assigned to
     * @return Returns the passed argument that needs to have information stored in (Competition)
     */
    public Competition addNewCompetition(Scanner keyboard, int compCount,
                                         Competition newCompetition) {
        System.out.println("Competition name: ");
        newCompetition.setCompName(keyboard.nextLine());
        newCompetition.setCompId(compCount);
        System.out.println("A new competition has been created!");
        System.out.print("Competition ID: " +
                newCompetition.getCompId() + ", Competition Name: "
                + newCompetition.getCompName() + ", Type: ");
        if (newCompetition.getIsLuckyMode()) {
            System.out.println("LuckyNumbersCompetition");
        } else if (!newCompetition.getIsLuckyMode()) {
            System.out.println("RandomPickCompetition");
        }
        // Tracks active status
        activeStatus = true;
        seed = 0;
        return newCompetition;
    }

    /**
     * Method loop and branches into different options until exit option is selected
     *
     * @param keyboard Scanner class to input
     * @param sc       Instance variable that stores every Competition and Entries
     * @param data     DataProvider class that holds lists of Bill and Member class from file input
     */
    public void console(Scanner keyboard, SimpleCompetitions sc, DataProvider data) {
        mainloop:
        while (true) {
            // When one case is done, they break back to this mainloop
            // local var compCount used to set new competition's id and always referencable
            int compCount = compList.size() + 1;
            printConsoleMsg();
            int option = keyboard.nextInt();
            // Skip the previous line (where nextInt/option is read but not moved on to next line)
            keyboard.nextLine();
            // Ready for different options and call respective methods while adhering to conditions
            // such as overall competition's active status, no. of list, no. of comp etc
            // If guards are not triggered it will run until it breaks back to mainloop
            switch (option) {
                case 1:
                    // activeStatus true means active and can't add anymore entries
                    if (activeStatus) {
                        System.out.println("There is an active competition. " +
                                "SimpleCompetitions does not support concurrent competitions!");
                        break;
                    }
                    System.out.println("Type of competition (L: LuckyNumbers, R: RandomPick)?:");
                    String compType = keyboard.next();
                    while ((!compType.equalsIgnoreCase("L")) &&
                            (!compType.equalsIgnoreCase("R"))) {
                        System.out.println("Invalid competition type! Please choose again.");
                        System.out.println("Type of competition (L: LuckyNumbers, " +
                                "R: RandomPick)?:");
                        compType = keyboard.next();
                    }
                    // set competition type and mode then create competition and add to the list
                    keyboard.nextLine();
                    if (compType.equalsIgnoreCase("L")) {
                        Competition newCompetition = new LuckyNumbersCompetition();
                        newCompetition.isLuckyMode(compType);
                        newCompetition.setTestingMode(testingMode);
                        currentComp = sc.addNewCompetition(keyboard, compCount, newCompetition);
                    } else if (compType.equalsIgnoreCase("R")) {
                        Competition newCompetition = new RandomPickCompetition();
                        newCompetition.isLuckyMode(compType);
                        newCompetition.setTestingMode(testingMode);
                        currentComp = sc.addNewCompetition(keyboard, compCount, newCompetition);
                    }
                    compList.add(currentComp);
                    break;
                case 2:
                    // Competition not active, can't add any entries
                    if (!activeStatus) {
                        System.out.println("There is no active competition. Please create one!");
                        break;
                    }
                    // Add entries to the current competition
                    if (currentComp != null) {
                        currentComp.addEntries(keyboard, data, sc);
                    }
                    break;
                case 3:
                    // Competition not active, can't draw winners
                    if (!activeStatus) {
                        System.out.println("There is no active competition. Please create one!");
                        break;
                    }
                    // No entries to draw winners
                    if (currentComp != null) {
                        if (currentComp.getEntryList().size() == 0) {
                            System.out.println("The current competition has no entries yet!");
                            break;
                        }
                        currentComp.drawWinners(sc);
                        // Competition status is marked completed
                        currentComp.setCompStatus(false);
                        activeStatus = false;
                    }
                    break;
                case 4:
                    // No competitions to be summarised
                    if (compList.size() == 0) {
                        System.out.println("No competition has been created yet!");
                        break;
                    }
                    // Invoke method for summary
                    report(sc);
                    break;
                case 5:
                    break mainloop;
                // Options for none of the provided options, print error message
                default:
                    System.out.println("Unsupported option. Please try again!");
            }
        }
    }

    /**
     * Method to generate report for the user using same instance variable of SimpleCompetitions
     *
     * @param sc SimpleCompetitions class that holds the information required to do report
     */
    public void report(SimpleCompetitions sc) {
        int completedComp = 0, activeComp = 0;
        // iterate through ArrayList of <Competition> to count completed and active competitions
        for (Competition c : sc.compList) {
            if (!c.getCompStatus()) {
                completedComp++;
            }
        }
        for (Competition c : sc.compList) {
            if (c.getCompStatus()) {
                activeComp++;
            }
        }
        System.out.println("----SUMMARY REPORT----");
        System.out.println("+Number of completed competitions: " + completedComp);
        System.out.println("+Number of active competitions: " + activeComp);
        // Checks the competition's active status and prints out details accordingly by iterating
        // through the ArrayList of type <Competition> that stores all the competitions
        String yOrN;
        for (Competition c : sc.compList) {
            if (c.getCompStatus()) {
                yOrN = "yes";
            } else {
                yOrN = "no";
            }
            System.out.println();
            System.out.printf("Competition ID:%2d, name: %s, active: %s\n",
                    c.getCompId(), c.getCompName(), yOrN);
            System.out.printf("Number of entries: %d\n", c.getEntryCounter());
            if (!c.getCompStatus()) {
                System.out.printf("Number of winning entries: %d\n", c.getNumOfWonEntries());
                System.out.printf("Total awarded prizes: %d\n", c.getAwardedPrizes());
            }
            // total values like numOfWonEntries and awardedPrizes are tracked throughout the code
        }
    }

    // Helper method to print repeated console messages
    public void printConsoleMsg() {
        System.out.println("Please select an option. Type 5 to exit.");
        System.out.println("1. Create a new competition");
        System.out.println("2. Add new entries");
        System.out.println("3. Draw winners");
        System.out.println("4. Get a summary report");
        System.out.println("5. Exit");
    }

    /**
     * Method to save output into a file or not and also update the csv file if needed
     *
     * @param keyboard     Scanner Class to input
     * @param sc           SimpleCompetitions class containing all the information
     * @param data         DataProvider class holding all Bill and Member class information
     * @param billFileName Name of the bills file when accessed and that should be updated
     */
    public void safeFile(Scanner keyboard, SimpleCompetitions sc,
                         DataProvider data, String billFileName) {
        // Ask to save to a file or not before exiting program
        System.out.println("Save competitions to file? (Y/N)?");
        String saveOrNo = keyboard.next();
        while ((!saveOrNo.equalsIgnoreCase("Y")) &&
                (!saveOrNo.equalsIgnoreCase("N"))) {
            System.out.println("Invalid mode! Please choose again.");
            System.out.println("Save competitions to file? (Y/N)?");
            saveOrNo = keyboard.next();
        }
        if (saveOrNo.equalsIgnoreCase("Y")) {
            System.out.println("File name:");
            keyboard.nextLine(); // skips the previous un-skipped line from save Y/N
            // try catch to handle exceptions from object output and write output
            try {
                String fileOutputName = keyboard.nextLine();
                // Output all serializable objects
                ObjectOutputStream objOutput =
                        new ObjectOutputStream(new FileOutputStream(fileOutputName));
                objOutput.writeObject(sc);
                objOutput.close();
                // Method from DataProvider to write file with updated objects and PrintWriter
                data.updateFile(billFileName);
            } catch (IOException e) {
                System.out.println("Problem with file output");
                System.exit(0);
            }
            System.out.println("Competitions have been saved to file.");
            System.out.println("The bill file has also been automatically updated.");
        }
    }

    /**
     * Main program that uses the main SimpleCompetitions class
     *
     * @param args main program arguments
     */
    public static void main(String[] args) {
        //Create an object of the SimpleCompetitions class
        Scanner keyboard = new Scanner(System.in);
        SimpleCompetitions sc = new SimpleCompetitions();
        String isTest, memFileName, billFileName;

        // Welcoming messages
        System.out.println("----WELCOME TO SIMPLE COMPETITIONS APP----");
        System.out.println("Load competitions from file? (Y/N)?");

        // Ask to load file
        String loadOrNo = keyboard.next();
        while ((!loadOrNo.equalsIgnoreCase("Y")) &&
                (!loadOrNo.equalsIgnoreCase("N"))) {
            System.out.println("Unsupported option. Please try again!");
            System.out.println("Load competitions from file? (Y/N)?");
            loadOrNo = keyboard.next();
        }

        // Ask for mode if didn't load binary data file
        if ((loadOrNo.equalsIgnoreCase("N"))) {
            System.out.println("Which mode would you like to run? " +
                    "(Type T for Testing, and N for Normal mode):");
            isTest = keyboard.next();
            while ((!isTest.equalsIgnoreCase("T")) &&
                    (!isTest.equalsIgnoreCase("N"))) {
                System.out.println("Invalid mode! Please choose again.");
                System.out.println("Which mode would you like to run? " +
                        "(Type T for Testing, and N for Normal mode):");
                isTest = keyboard.next();
            }
            // set testing mode
            sc.isTestingMode(isTest);
            keyboard.nextLine();

        } else if ((loadOrNo.equalsIgnoreCase("Y"))) {
            // ask for file sources after skipping the end of line from previous next()
            keyboard.nextLine();
            System.out.println("File name:");
            String fileName = keyboard.nextLine();
            // try catch for exceptions
            try {
                ObjectInputStream objInput = new ObjectInputStream(new FileInputStream(fileName));
                sc = (SimpleCompetitions) objInput.readObject();
            } catch (FileNotFoundException e) {
                System.out.println("Cannot find data file.");
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("Problems with reading objects from file.");
                System.exit(0);
            } catch (IOException e) {
                System.out.println("Problems with file input.");
                System.exit(0);
            }
        }

        // Ask for csv data files
        System.out.println("Member file: ");
        memFileName = keyboard.nextLine();
        System.out.println("Bill file: ");
        billFileName = keyboard.nextLine();

        // Use DataProvider class to load data and store list of objects of Member, Bill classes
        DataProvider data = null;
        try {
            data = new DataProvider(memFileName, billFileName);
        } catch (DataAccessException | DataFormatException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }

        // Go to the console (options)
        sc.console(keyboard, sc, data);

        // Safe to file method
        sc.safeFile(keyboard, sc, data, billFileName);

        System.out.println("Goodbye!");
        keyboard.close();
    }
}