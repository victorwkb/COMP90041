import java.util.Scanner;

public class DrawingCanvas {
    public void changeSettings(Scanner input) {
        // Ask for new details
        System.out.print("Canvas width: ");
        ConsoleDrawing.width = input.nextInt();
        System.out.print("Canvas height: ");
        ConsoleDrawing.height = input.nextInt();
        System.out.print("Background character: ");
        ConsoleDrawing.bgChar = input.next();

        // Print updated settings
        System.out.print("Drawing canvas has been updated!\n\n");
        System.out.println("Current drawing canvas settings:");
        System.out.println("- Width: " + ConsoleDrawing.width);
        System.out.println("- Height: " + ConsoleDrawing.height);
        System.out.println("- Background character: " + ConsoleDrawing.bgChar);
        System.out.println();

        // Make new instance variables again to access its methods (recursive)
        ConsoleDrawing cd = new ConsoleDrawing();
        cd.runConsole(input);
        input.close();
    }
}
