package ddip.me.ddipbe.application.exception;

public class NotEventOwnerException extends RuntimeException {
    public NotEventOwnerException() {
        super("이벤트 주최자가 아닙니다.");
    }
}
