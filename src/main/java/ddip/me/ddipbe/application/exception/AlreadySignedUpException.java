package ddip.me.ddipbe.application.exception;

public class AlreadySignedUpException extends RuntimeException {
    public AlreadySignedUpException() {
        super("이미 가입된 이메일입니다.");
    }
}
