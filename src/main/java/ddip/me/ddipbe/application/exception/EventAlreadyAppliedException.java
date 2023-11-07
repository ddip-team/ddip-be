package ddip.me.ddipbe.application.exception;

public class EventAlreadyAppliedException extends RuntimeException {
    public EventAlreadyAppliedException() {
        super("이미 신청한 이벤트입니다.");
    }
}
