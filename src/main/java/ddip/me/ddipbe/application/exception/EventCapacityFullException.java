package ddip.me.ddipbe.application.exception;

public class EventCapacityFullException extends RuntimeException {
    public EventCapacityFullException() {
        super("이벤트 정원이 가득 찼습니다.");
    }
}
