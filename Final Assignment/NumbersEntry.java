/*
 * Student name: Kai Bing Goh
 * Student ID: 981436
 * LMS username: kaibingg
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * A child class of Entry class where manual entries are inputted through methods of this class
 */
public class NumbersEntry extends Entry {
    /**
     * Method to set the numbers of entries which requires manual input from user
     *
     * @param keyboard Scanner class to input
     * @param sc       SimpleCompetition class that stores all the information
     */
    public void setManualNumbers(Scanner keyboard, SimpleCompetitions sc) {
        System.out.println("Please enter 7 different numbers " +
                "(from the range 1 to 35) separated by whitespace.");
        // Loop until valid manual entries are inserted
        while (true) {
            // Using StringTokenizer to separate string into different components and parse it
            // into an ArrayList of type <Integer>
            int i = 0;  // index i counts the number inputs in that line
            ArrayList<Integer> manualEntList = new ArrayList<Integer>();
            String line = keyboard.nextLine();
            String[] tokens = line.split("\\s+");
            boolean flag = true;
            // checks the line consists of valid int, else skip rest of code and restart loop
            for (int j = 0; j < tokens.length; j++) {
                if (isInt(tokens[j])) {
                    manualEntList.add(Integer.parseInt(tokens[j]));
                    i++;
                    if (Integer.parseInt(tokens[j]) > 35) {
                        System.out.println("Invalid input! All numbers must " +
                                "be in the range from 1 to 35!");
                        System.out.println("Please enter 7 different numbers " +
                                "(from the range 1 to 35) separated by whitespace.");
                        flag = false;
                        break;
                    }
                } else if (!isInt(tokens[j])) {
                    System.out.println("Invalid input! Numbers are expected. Please try again!");
                    System.out.println("Please enter 7 different numbers " +
                            "(from the range 1 to 35) separated by whitespace.");
                    flag = false;
                    break;
                }
            }
            if (!flag) {
                continue; // skips rest of code
            }
            // Converting into an int array
            int[] manualEnt = new int[manualEntList.size()];
            for (int index = 0; index < manualEntList.size(); index++) {
                manualEnt[index] = manualEntList.get(index);
            }
            // Checks for correct amount of int entries
            if (i < 7) {
                System.out.println("Invalid input! Fewer than 7 numbers are provided. " +
                        "Please try again!");
                System.out.println("Please enter 7 different numbers " +
                        "(from the range 1 to 35) separated by whitespace.");
            } else if (i > 7) {
                System.out.println("Invalid input! More than 7 numbers are provided. " +
                        "Please try again!");
                System.out.println("Please enter 7 different numbers " +
                        "(from the range 1 to 35) separated by whitespace.");
            } else if (duplicateInt(manualEnt)) {
                System.out.println("Invalid input! All numbers must be different!");
                System.out.println("Please enter 7 different numbers " +
                        "(from the range 1 to 35) separated by whitespace.");
            } else {
                // No errors, sort the array and the <Entry> Object will copy and store the data
                Arrays.sort(manualEnt);
                super.setNumbers(manualEnt);
                // Every entry updates the static seed variable
                sc.updateSeed();
                break;
            }
        }
    }

    //  Method to check for duplicates in the array itself, returns true if duplicates detected
    //  No duplicates returns false
    public boolean duplicateInt(int[] manualEnt) {
        for (int i = 0; i < manualEnt.length; i++) {
            for (int j = i + 1; j < manualEnt.length; j++) {
                if (manualEnt[i] == manualEnt[j]) {
                    return true;
                }
            }
        }
        return false;
    }

    // Method checks if the string is convertible to int
    public boolean isInt(String str) {
        try {
            int x = Integer.parseInt(str);
            return true; //String is an Integer
        } catch (NumberFormatException e) {
            return false; //String is not an Integer
        }
    }
}