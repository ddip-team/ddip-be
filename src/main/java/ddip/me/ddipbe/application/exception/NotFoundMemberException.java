package ddip.me.ddipbe.application.exception;

public class NotFoundMemberException extends RuntimeException{
    public NotFoundMemberException() {
        super("Member not found in the database.");
    }

    public NotFoundMemberException(String message) {
        super(message);
    }
}
