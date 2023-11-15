package ddip.me.ddipbe.application.exception;

public class SuccessRecordNotFoundException extends RuntimeException {
    public SuccessRecordNotFoundException() {
        super("이벤트 성공 기록이 존재하지 않습니다.");
    }
}
