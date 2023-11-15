package ddip.me.ddipbe.application.exception;

public class SuccessFormAlreadyRegisteredException extends RuntimeException {
    public SuccessFormAlreadyRegisteredException() {
        super("이미 폼이 등록되었습니다.");
    }
}
