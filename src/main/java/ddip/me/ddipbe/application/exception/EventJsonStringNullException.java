package ddip.me.ddipbe.application.exception;

public class EventJsonStringNullException extends RuntimeException{
    public EventJsonStringNullException() {
        super("이벤트 참여자 폼이 null 값입니다.");
    }
}
