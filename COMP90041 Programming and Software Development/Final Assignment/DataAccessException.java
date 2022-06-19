/*
 * Student name: Kai Bing Goh
 * Student ID: 981436
 * LMS username: kaibingg
 */

/**
 * Exception class that gets thrown when there is error in data access
 */
public class DataAccessException extends Exception {
    // Method to output error message when exception is thrown
    public DataAccessException(String errorMessage) {
        super(errorMessage);
    }

    // Default error message when no arguments
    public DataAccessException() {
        super("DataAccessException");
    }
}
