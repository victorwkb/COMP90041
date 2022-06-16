import java.util.Scanner;

// Note: input.close() are added to make ensure that the 
// scanner file is successfully close with not errors

public class ConsoleDrawing {
    // Constant Canvas Variables for triangle.java and square.java (unless updated
    // in DrawingCanvas.java)
    public static int width;
    public static int height;
    public static String bgChar;

    // Method to print the list of user options
    public void listOptions() {
        System.out.println("Please select an option. Type 4 to exit.");
        System.out.println("1. Draw triangles");
        System.out.println("2. Draw squares");
        System.out.println("3. Update drawing canvas settings");
        System.out.println("4. Exit");
    }

    // (Console) Method to print invalid option alert and prompt until user chooses
    // valid option
    public void runConsole(Scanner input) {
        // Set new instance variables to call non-static methods
        Triangle tri = new Triangle();
        Square sqr = new Square();
        DrawingCanvas dc = new DrawingCanvas();

        listOptions();
        int option = input.nextInt();
        while (option != 1 && option != 2 && option != 3 && option != 4) {
            System.out.println("Unsupported option. Please try again!");
            listOptions();
            option = input.nextInt();
        }
        if (option == 1) {
            tri.runTriangleProgram(input);
            input.close();

        } else if (option == 2) {
            sqr.runSquareProgram(input);
            input.close();

        } else if (option == 3) {
            dc.changeSettings(input);
            input.close();

        } else if (option == 4) {
            System.out.println("Goodbye!");
            input.close();
            System.exit(0);
        }
    }

    // Below are the only static methods called (<3) in all .java files (main included)

    // Method to print out any matrix passed into it as argument, used in
    // Triangle.java and Square.java
    public static void printMatrix(String[][] matrix) {
        for (int j = 0; j < ConsoleDrawing.height; j++) {
            for (int i = 0; i < ConsoleDrawing.width; i++) {
                System.out.printf("%s", matrix[i][j]);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        // Parse arguments into static variables
        width = Integer.parseInt(args[0]);
        height = Integer.parseInt(args[1]);
        bgChar = args[2];

        // Initialise scanner variable to be passed into methods later
        Scanner input = new Scanner(System.in);
        // Set new instance variable to call non-static methods
        ConsoleDrawing cd = new ConsoleDrawing();

        // Welcoming messages
        System.out.println("----WELCOME TO MY CONSOLE DRAWING APP----");
        System.out.println("Current drawing canvas settings:");
        System.out.println("- Width: " + width);
        System.out.println("- Height: " + height);
        System.out.println("- Background character: " + bgChar);
        System.out.println();

        // Start the console
        cd.runConsole(input);
    }
}