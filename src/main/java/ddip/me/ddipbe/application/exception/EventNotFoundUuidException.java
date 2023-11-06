package ddip.me.ddipbe.application.exception;

import lombok.Getter;

@Getter
public class EventNotFoundUuidException extends RuntimeException {
    public EventNotFoundUuidException() {
        super("UUID not found in the database.");
    }

    public EventNotFoundUuidException(String message) {
        super(message);
    }
}
