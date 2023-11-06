package ddip.me.ddipbe.global.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("인증되지 않은 사용자입니다.");
    }
}
