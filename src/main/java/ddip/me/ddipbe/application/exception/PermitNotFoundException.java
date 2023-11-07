package ddip.me.ddipbe.application.exception;

public class PermitNotFoundException extends RuntimeException {
    public PermitNotFoundException() {
        super("이벤트 성공 기록이 존재하지 않습니다.");
    }
}
