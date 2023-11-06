package ddip.me.ddipbe.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class NotFoundUuidException extends RuntimeException{
    public NotFoundUuidException() {
        super("UUID not found in the database.");
    }

    public NotFoundUuidException(String message) {
        super(message);
    }
}
