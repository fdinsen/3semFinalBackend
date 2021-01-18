package errorhandling;

/**
 *
 * @author lam@cphbusiness.dk
 */
public class InvalidInput extends Exception {

    public InvalidInput(String message) {
        super(message);
    }

    public InvalidInput() {
        super("Requested item could not be found");
    }
}
