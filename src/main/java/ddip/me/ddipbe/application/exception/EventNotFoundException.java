package ddip.me.ddipbe.application.exception;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException() {
        super("존재하지 않는 이벤트입니다.");
    }
}
