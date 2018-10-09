package HardwareStore.HardwareStore;

/**
 * Custom Exception type, used to report bad input from user.
 */
public class BadInputException extends Exception {
    
    /**
     * Constructor, allows custom message assignment for thrown exception.
     * @param message custom message assignment
     */
    public BadInputException(String message) {
        super(message);
    }
}