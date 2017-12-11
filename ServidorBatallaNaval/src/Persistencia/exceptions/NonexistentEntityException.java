package Persistencia.exceptions;

/**
 *
 * @author Irvin Dereb Vera López.
 * @author Israel Reyes Ozuna.
 */
public class NonexistentEntityException extends Exception {

    public NonexistentEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonexistentEntityException(String message) {
        super(message);
    }
}
