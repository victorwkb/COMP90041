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

import java.util.ArrayList;
import java.util.Scanner;

public class SimpleCompetitions {
    //  declare and initialise values for later use
    public static int seed = 0;
    public ArrayList<Competition> compList = new ArrayList<Competition>();
    public boolean activeStatus = false;

    //  Method to add competitions and print information out when a competition is added
    public Competition addNewCompetition(Scanner keyboard, int compCount) {
        Competition newCompetition = new Competition();
        System.out.println("Competition name: ");
        newCompetition.setCompName(keyboard.nextLine());
        newCompetition.setCompId(compCount);
        System.out.println("A new competition has been created!");
        System.out.println("Competition ID: " +
                newCompetition.getCompId() + ", Competition Name: "
                + newCompetition.getCompName());
//      Tracks active status
        activeStatus = true;
        return newCompetition;
    }

    public static void main(String[] args) {
        Scanner keyboard = new Scanner(System.in);
        SimpleCompetitions sc = new SimpleCompetitions();

        System.out.println("----WELCOME TO SIMPLE COMPETITIONS APP----");
        System.out.println("Which mode would you like to run? " +
                "(Type T for Testing, and N for Normal mode):");
        String t_n = keyboard.next();
        while ((!t_n.equalsIgnoreCase("T")) &&
                (!t_n.equalsIgnoreCase("N"))) {
            System.out.println("Invalid mode! Please choose again.");
            System.out.println("Which mode would you like to run? " +
                    "(Type T for Testing, and N for Normal mode):");
            t_n = keyboard.next();
        }
//      Pass the intended mode as a string (t/n) into the console method to use the string and
//      decide on which method to call later
        sc.console(keyboard, sc, t_n);
//      After finishing the main loop from sc.console above, program finishes
        System.out.println("Goodbye!");
        keyboard.close();
    }

//  Method branches into loop for different options until exit option is selected
    public void console(Scanner keyboard, SimpleCompetitions sc, String t_n) {
        Competition newComp = null;
        mainloop:
        while (true) {
//          when one case is done, they break back to this mainloop
            int compCount = compList.size() + 1;
            System.out.println("Please select an option. Type 5 to exit.");
            System.out.println("1. Create a new competition");
            System.out.println("2. Add new entries");
            System.out.println("3. Draw winners");
            System.out.println("4. Get a summary report");
            System.out.println("5. Exit");
            int option = keyboard.nextInt();
//          Skip the previous line (where nextInt/option is read but not moved on to next line)
            keyboard.nextLine();
//          Ready to go to different cases and call different methods but with conditions to check
//          such as overall competition's active status, no. of list, no. of comp etc

//          If guards are not triggered it will run until it breaks back to mainloop
            switch (option) {
                case 1:
//                  activeStatus true means active and can't add anymore entries
                    if (activeStatus) {
                        System.out.println("There is an active competition. " +
                                "SimpleCompetitions does not support concurrent competitions!");
                        break;
                    }
                    newComp = (sc.addNewCompetition(keyboard, compCount));
                    compList.add(newComp);
                    break;
                case 2:
//                  Competition not active can't add any entries
                    if (!activeStatus) {
                        System.out.println("There is no active competition. Please create one!");
                        break;
                    }
                    newComp.addEntries(keyboard, t_n);
                    break;
                case 3:
//                  Competition not active so can't get summary
                    if (!activeStatus) {
                        System.out.println("There is no active competition. Please create one!");
                        break;
                    }
//                  No entries to summarise
                    if (newComp.entryList.size() == 0) {
                        System.out.println("The current competition has no entries yet!");
                        break;
                    }
                    newComp.drawWinners(t_n);
                    newComp.compStatus = false;
                    activeStatus = false;
                    break;
                case 4:
//                  No competitions
                    if (compList.size() == 0) {
                        System.out.println("No competition has been created yet!");
                        break;
                    }
                    newComp.report(keyboard, sc);
                    break;
                case 5:
                    break mainloop;
//              None of the supported cases, so print warning
                default:
                    System.out.println("Unsupported option. Please try again!");
            }
        }
    }
}