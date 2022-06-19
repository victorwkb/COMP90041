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
import java.util.Collections;
import java.util.Random;

public class AutoEntry extends Entry {
//  createNumbers method for testing mode (takes seeds as inputs)
    public int[] createNumbers (int seed) {
        ArrayList<Integer> validList = new ArrayList<Integer>();
        int[] tempNumbers = new int[7];
        for (int i = 1; i <= 35; i++) {
            validList.add(i);
        }
        Collections.shuffle(validList, new Random(seed));
        for (int i = 0; i < 7; i++) {
            tempNumbers[i] = validList.get(i);
        }
        Arrays.sort(tempNumbers);
        return tempNumbers;
    }

//  createNumbers method for normal mode (doesn't take any seed, only random)
    public int[] createNumbers () {
        ArrayList<Integer> validList = new ArrayList<Integer>();
        int[] tempNumbers = new int[7];
        for (int i = 1; i <= 35; i++) {
            validList.add(i);
        }
        Collections.shuffle(validList, new Random());
        for (int i = 0; i < 7; i++) {
            tempNumbers[i] = validList.get(i);
        }
        Arrays.sort(tempNumbers);
        return tempNumbers;
    }
}