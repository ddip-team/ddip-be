package ddip.me.ddipbe.application.exception;

public class EventNotFoundException extends RuntimeException {

    public EventNotFoundException() {
        super("Event not found in the database.");
    }

    public EventNotFoundException(String message) {
        super(message);
    }
}
