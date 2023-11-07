package ddip.me.ddipbe.application.exception;

public class EventNotOpenException extends RuntimeException {
    public EventNotOpenException() {
        super("이벤트 기간이 아닙니다.");
    }
}
