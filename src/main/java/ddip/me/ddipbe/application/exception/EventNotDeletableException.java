package ddip.me.ddipbe.application.exception;

public class EventNotDeletableException extends RuntimeException {
    public EventNotDeletableException() {
        super("이벤트를 삭제할 수 없습니다.");
    }
}
