import java.util.Scanner;

// Note:    structure are similar to Triangle.java but slightly different, comments 
//          are kept the same for some parts for ease of understanding

public class Square {
    // Declare class objects to be used locally within .java file
    public int sideLen;
    public int col_index;
    public String pChar;
    public String alignment;

    // Start of square sub-program, reading inputs
    public void runSquareProgram(Scanner input) {
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
            printSquare(input, col_index);
        } else if (alignment.equalsIgnoreCase("right")) {
            col_index = ConsoleDrawing.width - sideLen + 1;
            printSquare(input, col_index);
        } else {
            printSquare(input, col_index);
        }
        input.close();

    }

    // Method that prints the first completed canvas out, disintegrated into smaller
    // methods for simplicity and for each methods' specific purpose later
    public void printSquare(Scanner input, int index) {
        // Starts with original side length (side length will be updated later)
        String[][] square = buildSquareMatrix(sideLen);
        String[][] canvas = insertSquare(square, index, sideLen);
        ConsoleDrawing.printMatrix(canvas);
        zoomSquare(input, square, sideLen, col_index);
    }

    // Method to build matrix of the square's side length (passed in as argument)
    public String[][] buildSquareMatrix(int sideLenIn) {
        String[][] square = new String[sideLenIn][sideLenIn];
        for (int j = 0; j < sideLenIn; j++) {
            for (int i = 0; i < sideLenIn; i++) {
                square[i][j] = pChar;
            }
        }
        return square;
    }

    // Insert the built matrix into the Canvas (replace occupied spots) with
    // updatable dimension and index
    public String[][] insertSquare(String[][] square, int index, int sideLenIn) {
        String[][] canvas = new String[ConsoleDrawing.width][ConsoleDrawing.height];
        int x = 0, y = 0;
        for (int j = 0; j < ConsoleDrawing.height; j++) {
            for (int i = 0; i < ConsoleDrawing.width; i++) {
                // Fills matrix in row by row starting at col_index when it is not empty
                // x and y runs from 0 through to square's side length - 1, x represents column
                if ((x < sideLenIn) && (y < sideLenIn) && 
                    (i >= index - 1) && (square[x][y] != null)) {
                    canvas[i][j] = square[x][y];
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

    // Method to print out the new canvas consisting zoomed in/out square according
    // to max/min dimension conditions
    public void zoomSquare(Scanner input, String[][] squareMat, int sideLenIn, int index) {
        // Create local temporary matrices
        String[][] newCanvas = new String[ConsoleDrawing.width][ConsoleDrawing.height];
        System.out.println("Type I/O to zoom in/out. Use other keys to continue.");
        // Reads zoom instructions (in/out)
        String zoom = input.next();

        // zooming in
        if (zoom.equalsIgnoreCase("i")) {
            // update new width/height/col_index
            int newSideLen = sideLenIn + 1;
            int newIndex;
            if (index == 1) {
                newIndex = index;

            } else if (index == (ConsoleDrawing.width - sideLenIn + 1)) {
                newIndex = index - 1;

            } else {
                newIndex = (ConsoleDrawing.width - newSideLen) / 2 + 1;
            }

            // check if max dimensions reached, if it is then print old canvas, otherwise
            // side length +1
            if ((newSideLen <= ConsoleDrawing.width) && (newSideLen <= ConsoleDrawing.height)) {
                String[][] newSqrMat = buildSquareMatrix(newSideLen);
                // put the matrix into new canvas according to the new col_index & width &
                // height
                newCanvas = insertSquare(newSqrMat, newIndex, newSideLen);
                ConsoleDrawing.printMatrix(newCanvas);
                // recurs method to accept more zooming instruction
                zoomSquare(input, newSqrMat, newSideLen, newIndex);

            } else {
                // print old unchanged canvas because maximising zoom limit reached (due to
                // height/width)
                newCanvas = insertSquare(squareMat, index, sideLenIn);
                ConsoleDrawing.printMatrix(newCanvas);
                zoomSquare(input, squareMat, sideLenIn, index);
            }
            input.close();
        }
        // zooming out
        else if (zoom.equalsIgnoreCase("o")) {
            // update new width/height/col_index
            int newSideLen = sideLenIn - 1;
            int newIndex;
            if (index == 1) {
                newIndex = index;

            } else if (index == (ConsoleDrawing.width - sideLenIn + 1)) {
                newIndex = index + 1;

            } else {
                newIndex = (ConsoleDrawing.width - newSideLen) / 2 + 1;
            }

            // check if min dimensions reached, if it is then print old canvas, otherwise
            // side length -1
            if (newSideLen >= 1) {
                String[][] newSqrMat = new String[newSideLen][newSideLen];
                newSqrMat = buildSquareMatrix(newSideLen);
                // put the matrix into new canvas according to the new col_index & width &
                // height
                newCanvas = insertSquare(newSqrMat, newIndex, newSideLen);
                ConsoleDrawing.printMatrix(newCanvas);
                // recurs method to accept more zooming instruction
                zoomSquare(input, newSqrMat, newSideLen, newIndex);

            } else {
                // print old unchanged canvas because minimising zoom limit reached (side length
                // = 1)
                newCanvas = insertSquare(squareMat, index, sideLenIn);
                ConsoleDrawing.printMatrix(newCanvas);
                zoomSquare(input, squareMat, sideLenIn, index);
            }
            input.close();

        } else {
            drawNewSquare(input);
        }
    }

    // Method to ask if a new square is to be drawn
    public void drawNewSquare(Scanner input) {
        System.out.println("Draw another square (Y/N)?");
        String y_n = input.next();

        // Prompt error message until valid input is entered
        while ((!y_n.equalsIgnoreCase("y")) && (!y_n.equalsIgnoreCase("n"))) {
            System.out.println("Unsupported option. Please try again!");
            System.out.println("Draw another square (Y/N)?");
            y_n = input.next();
        }
        if (y_n.equalsIgnoreCase("y")) {
            runSquareProgram(input);

        } else if (y_n.equalsIgnoreCase("n")) {
            ConsoleDrawing cd = new ConsoleDrawing();
            cd.runConsole(input);
        }
        input.close();
    }
}
