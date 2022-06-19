import java.util.Scanner;

public class Triangle {
    // Declare class objects to be used locally within .java file
    public int sideLen;
    public int col_index;
    public String pChar;
    public String alignment;

    // Start of triangle sub-program, reading inputs
    public void runTriangleProgram(Scanner input) {
        // Prompt for details
        System.out.println("Side length:");
        sideLen = input.nextInt();

        // Prompt error message until valid dimensions entered
        while (sideLen > ConsoleDrawing.width || sideLen > ConsoleDrawing.height) {
            System.out.println("Error! The side length is too long (Current canvas size is " 
                + ConsoleDrawing.width + "x" + ConsoleDrawing.height + "). Please try again.");
            System.out.println("Side length:");
            sideLen = input.nextInt();
        }
        System.out.println("Printing character:");
        pChar = input.next();
        System.out.println("Alignment (left, middle, right):");
        alignment = input.next();
        col_index = 1;

        // Checks how it should be aligned
        if (alignment.equalsIgnoreCase("middle")) {
            col_index = (ConsoleDrawing.width - sideLen) / 2 + 1;
            printTriangle(input);
        } else if (alignment.equalsIgnoreCase("right")) {
            col_index = ConsoleDrawing.width - sideLen + 1;
            printTriangle(input);
        } else {
            printTriangle(input);
        }
        input.close();
    }

    // Method that prints the first completed canvas out, disintegrated into smaller
    // methods for simplicity and for each methods' specific purpose later
    public void printTriangle(Scanner input) {
        String[][] triangle = buildTriangleMatrix();
        String[][] canvas = insertTriangle(triangle);
        ConsoleDrawing.printMatrix(canvas);
        rotateTriangle(input, triangle);
    }

    // Method to build matrix of the triangle's side length (triangle in a square)
    public String[][] buildTriangleMatrix() {
        String[][] triangle = new String[sideLen][sideLen];
        for (int j = 0; j < sideLen; j++) {
            for (int i = 0; i < sideLen; i++) {
                if (i < (sideLen - j)) {
                    triangle[i][j] = pChar;
                } 
                // non-triangle elements are just background characters
                else {
                    triangle[i][j] = ConsoleDrawing.bgChar;
                }
            }
        }
        return triangle;
    }

    // Insert the built matrix into the Canvas (replace occupied spots)
    public String[][] insertTriangle(String[][] triangle) {
        String[][] canvas = new String[ConsoleDrawing.width][ConsoleDrawing.height];
        int x = 0, y = 0;
        for (int j = 0; j < ConsoleDrawing.height; j++) {
            for (int i = 0; i < ConsoleDrawing.width; i++) {
                // Fills matrix in row by row starting at col_index when it is not empty
                // x and y runs from 0 through to triangle's side length - 1, x represents column
                if ((x < sideLen) && (y < sideLen) && 
                    (i >= col_index - 1) && (triangle[x][y] != null)) {
                    canvas[i][j] = triangle[x][y];
                    x++;
                } else {
                    canvas[i][j] = ConsoleDrawing.bgChar;
                }
            }
            // new row so columns (x) resets to index 0
            x = 0;
            y++;
        }
        return canvas;
    }

    // Rotate the built triangle matrix and repeat inserting into new canvas to be
    // printed out
    public void rotateTriangle(Scanner input, String[][] triangleMat) {
        // Create local temporary matrices
        String[][] newTriMat = new String[sideLen][sideLen];
        String[][] newCanvas = new String[ConsoleDrawing.width][ConsoleDrawing.height];
        System.out.println(
            "Type R/L to rotate clockwise/anti-clockwise. Use other keys to continue.");
        // Reads rotation's direction
        String direction = input.next();

        // Clockwise rotation algorithm
        if (direction.equalsIgnoreCase("r")) {
            for (int j = 0; j < sideLen; j++) {
                for (int i = 0; i < sideLen; i++) {
                    newTriMat[sideLen - 1 - j][i] = triangleMat[i][j];
                }
            }
            // Prints rotated triangle into a new canvas, then printed out and recurs to
            // same method
            newCanvas = insertTriangle(newTriMat);
            ConsoleDrawing.printMatrix(newCanvas);
            rotateTriangle(input, newTriMat);
            input.close();
        }
        // Anti-clockwise rotation algorithm
        else if (direction.equalsIgnoreCase("l")) {
            for (int j = 0; j < sideLen; j++) {
                for (int i = 0; i < sideLen; i++) {
                    newTriMat[j][sideLen - 1 - i] = triangleMat[i][j];
                }
            }
            // Prints rotated triangle into a new canvas, then printed out and recurs to
            // same method
            newCanvas = insertTriangle(newTriMat);
            ConsoleDrawing.printMatrix(newCanvas);
            rotateTriangle(input, newTriMat);
            input.close();
        }
        // Any other input to exit rotating the triangle
        else {
            drawNewTriangle(input);
        }
    }

    // Method to ask if a new triangle is to be drawn
    public void drawNewTriangle(Scanner input) {
        System.out.println("Draw another triangle (Y/N)?");
        String y_n = input.next();

        // Prompt error message until valid input is entered
        while ((!y_n.equalsIgnoreCase("y")) && (!y_n.equalsIgnoreCase("n"))) {
            System.out.println("Unsupported option. Please try again!");
            System.out.println("Draw another triangle (Y/N)?");
            y_n = input.next();
        }
        if (y_n.equalsIgnoreCase("y")) {
            runTriangleProgram(input);

        } else if (y_n.equalsIgnoreCase("n")) {
            ConsoleDrawing cd = new ConsoleDrawing();
            cd.runConsole(input);
        }
        input.close();
    }
}