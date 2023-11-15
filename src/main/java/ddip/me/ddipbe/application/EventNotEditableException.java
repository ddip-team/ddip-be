package ddip.me.ddipbe.application;

public class EventNotEditableException extends RuntimeException {
    public EventNotEditableException() {
        super("이벤트 수정이 불가능합니다.");
    }
}
