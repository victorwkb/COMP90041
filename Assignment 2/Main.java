import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

/**
 * The Main class implements an application that reads lines from the standard input
 * and prints them to the standard output.
 */
public class Main {
    /**
     * Iterate through each line of input.
     */
    public static void main(String[] args) throws IOException {
        InputStreamReader reader = new InputStreamReader(System.in, StandardCharsets.UTF_8);
        BufferedReader in = new BufferedReader(reader);
        String line;
        String[] lineArray;
        Integer[] cards = new Integer[5];

        while ((line = in.readLine()) != null) {
            lineArray = line.split(" ");

            //make card into integer array
            for (int i = 0; i < lineArray.length; i++) {
                cards[i] = Integer.parseInt(lineArray[i]);
            }

            //parse into permute method in the form of a list
            permute(Arrays.asList(cards), 0);

            //successful ran through all iterations, no success so exit program
            System.out.println("NO");
            System.exit(0);
        }
    }

    // recursive method to check through all combinations of operations for a specific array
    // [var1,var2,var3,var4,var5] using the formula provided ((((val1 op1 val2) op2 val3) op3
    // val4) op4 val5) = 42
    public static boolean game(int[] cards, int count, int number) {
        if (count == 4) {
            return number == 42;
        }
        count++;
        // explore 3 different operation possibilities
        return game(cards, count, operate(number, cards[count], "plus")) ||
                game(cards, count, operate(number, cards[count], "times")) ||
                game(cards, count, operate(number, cards[count], "minus"));
    }

    // lazy method to parse the operators into the method using strings
    public static int operate(int x, int y, String op) {
        int total = switch (op) {
            case "times" -> x * y;
            case "plus" -> x + y;
            case "minus" -> x - y;
            default -> 0;
        };
        return total;
    }

    // this algorithm utilises libraries from java.util Collections,Arrays,Lists
    // algorithm to parse through all possible permutation of the array of length 5, for every
    // arrangement of the array, parse that array into the recursive method
    public static void permute(List<Integer> arr, int k) {
        for (int i = k; i < arr.size(); i++) {
            Collections.swap(arr, i, k);
            permute(arr, k + 1);
            Collections.swap(arr, k, i);
        }
        if (k == arr.size() - 1) {
            String Array = Arrays.toString(arr.toArray());
            int[] convertedArr = convert(Array);
            int count = 0;
            if (game(convertedArr, count, convertedArr[0])) {
                System.out.println("YES");
                System.exit(0);
            }
        }
    }

    // method to convert string to int array based on the result from .toArray
    public static int[] convert(String str) {
        str = str.replaceAll("\\[|\\]", "");
        String[] strArr = str.split(", ");
        int[] returnArray = new int[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            returnArray[i] = Integer.parseInt(strArr[i]);
        }
        return returnArray;
    }
}