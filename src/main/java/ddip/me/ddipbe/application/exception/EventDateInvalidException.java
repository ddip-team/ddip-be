package ddip.me.ddipbe.application.exception;

public class EventDateInvalidException extends RuntimeException {
    public EventDateInvalidException() {
        super("이벤트 날짜가 올바르지 않습니다.");
    }
}
