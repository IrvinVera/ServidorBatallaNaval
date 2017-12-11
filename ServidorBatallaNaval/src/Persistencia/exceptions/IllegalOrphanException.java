package Persistencia.exceptions;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Irvin Dereb Vera López.
 * @author Israel Reyes Ozuna.
 *
 */
public class IllegalOrphanException extends Exception {

    private List<String> messages;

    public IllegalOrphanException(List<String> messages) {
        super((messages != null && messages.size() > 0 ? messages.get(0) : null));
        if (messages == null) {
            this.messages = new ArrayList<String>();
        } else {
            this.messages = messages;
        }
    }

    public List<String> getMessages() {
        return messages;
    }
}
