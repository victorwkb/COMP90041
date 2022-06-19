/*
 * Student name: Kai Bing Goh
 * Student ID: 981436
 * LMS username: kaibingg
 */

/**
 * Exception class that gets thrown when there is error in data format
 */
public class DataFormatException extends Exception {
    // Method to output error message when exception is thrown
    public DataFormatException(String errorMessage) {
        super(errorMessage);
    }

    // Default error message when no arguments
    public DataFormatException() {
        super("DataFormatException");
    }
}
