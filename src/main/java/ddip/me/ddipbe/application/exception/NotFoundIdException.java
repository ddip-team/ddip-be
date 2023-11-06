package ddip.me.ddipbe.application.exception;

public class NotFoundIdException extends RuntimeException{

    public NotFoundIdException() {
        super("Id not found in the database.");
    }

    public NotFoundIdException(String message) {
        super(message);
    }
}
