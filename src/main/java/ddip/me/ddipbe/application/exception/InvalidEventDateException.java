package ddip.me.ddipbe.application.exception;

public class InvalidEventDateException extends RuntimeException{
    public InvalidEventDateException() {
        super("Event Date is not valid value");
    }

    public InvalidEventDateException(String message) {
        super(message);
    }
}
