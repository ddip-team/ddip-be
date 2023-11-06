package ddip.me.ddipbe.application.exception;

public class EventNotFoundIdException extends RuntimeException {

    public EventNotFoundIdException() {
        super("Id not found in the database.");
    }

    public EventNotFoundIdException(String message) {
        super(message);
    }
}
