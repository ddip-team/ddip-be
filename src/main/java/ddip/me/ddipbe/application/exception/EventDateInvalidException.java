package ddip.me.ddipbe.application.exception;

public class EventDateInvalidException extends RuntimeException {
    public EventDateInvalidException() {
        super("Event Date is not valid value");
    }

    public EventDateInvalidException(String message) {
        super(message);
    }
}
