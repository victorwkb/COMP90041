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
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Entry {
    private int entryId;
    private int memId;
    private int billId;
    private int[] numbers;
    public int autoStatus;
    public int prizeWon;

//  Setter and getter methods to access private data

    public void setEntryId(int anEntryId) {
        entryId = anEntryId;
    }

    public int getEntryId() {
        return entryId;
    }

    public void setMemId(int aMemId) {
        memId = aMemId;
    }

    public int getMemId() {
        return memId;
    }

    public void setBillId(int aBillId) {
        billId = aBillId;
    }

    public void setNumbers(int[] numbers) {
        this.numbers = numbers;
    }

    public int[] getNumbers() {
        return numbers;
    }

//  Method to deep copy all details of an <Entry> class
    public Entry getEntry() {
        Entry temp = new Entry();
        temp.entryId = entryId;
        temp.memId = memId;
        temp.billId = billId;
        temp.numbers = new int[7];
        for (int i = 0; i < 7; i++) {
            temp.numbers[i] = numbers[i];
        }
        temp.autoStatus = autoStatus;
        return temp;
    }

    public void setManualNumbers(Scanner keyboard) {
        System.out.println("Please enter 7 different numbers " +
                "(from the range 1 to 35) separated by whitespace.");
//      Loop until valid manual entries are inserted
        while (true) {
//          Using StringTokenizer to separate string into different components and parse it
//          into an ArrayList of type <Integer>
            int i = 0;  // index i counts the number of lines
            ArrayList<Integer> manualEntList = new ArrayList<Integer>();
            StringTokenizer st = new StringTokenizer(keyboard.nextLine());
            while (st.hasMoreTokens()) {
                String nextToken = st.nextToken();
                manualEntList.add(Integer.parseInt(nextToken));
                i++;
            }
//          Converting into an int array
            int[] manualEnt = new int[manualEntList.size()];
            for (int index = 0; index < manualEntList.size(); index++) {
                manualEnt[index] = manualEntList.get(index);
            }
//          Checks for validity if format of manual entries are adhered
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
//              No errors, sort the array and the <Entry> Object will copy and store the data
                Arrays.sort(manualEnt);
                numbers = manualEnt;
//              Every entry updates the static seed variable
                SimpleCompetitions.seed++;
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
}