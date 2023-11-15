package ddip.me.ddipbe.global.exception;

import ddip.me.ddipbe.application.exception.*;
import jakarta.validation.ConstraintViolationException;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Set;

@Getter
public enum ErrorCode {
    // Common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 생겼습니다. 관리자에게 문의하세요.", Set.of()),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력 값이 올바르지 않습니다.",
            Set.of(MethodArgumentNotValidException.class, ConstraintViolationException.class)),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "지원하지 않는 HTTP 메서드입니다.", Set.of(HttpRequestMethodNotSupportedException.class)),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다.", Set.of(UnauthorizedException.class)),

    // Member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원이 존재하지 않습니다.", Set.of(MemberNotFoundException.class)),
    ALREADY_SIGNED_UP(HttpStatus.BAD_REQUEST, "이미 가입된 이메일입니다.", Set.of(AlreadySignedUpException.class)),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.", Set.of(InvalidPasswordException.class)),

    // Event
    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "이벤트가 존재하지 않습니다.", Set.of(EventNotFoundException.class)),
    EVENT_ALREADY_APPLIED(HttpStatus.BAD_REQUEST, "이미 신청한 이벤트입니다.", Set.of(EventAlreadyAppliedException.class)),
    EVENT_CAPACITY_FULL(HttpStatus.BAD_REQUEST, "이벤트 정원이 가득 찼습니다.", Set.of(EventCapacityFullException.class)),
    EVENT_DATE_INVALID(HttpStatus.BAD_REQUEST, "이벤트 날짜가 올바르지 않습니다.", Set.of(EventDateInvalidException.class)),
    EVENT_NOT_OPEN(HttpStatus.BAD_REQUEST, "이벤트 기간이 아닙니다.", Set.of(EventNotOpenException.class)),
    SUCCESS_FORM_ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, "이미 폼이 등록되었습니다.", Set.of(SuccessFormAlreadyRegisteredException.class)),
    SUCCESS_RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, "이벤트 성공 기록이 존재하지 않습니다.", Set.of(SuccessRecordNotFoundException.class)),
    ;

    private final HttpStatusCode status;
    private final String code;
    private final String message;
    private final Set<Class<? extends Exception>> exceptions;

    ErrorCode(HttpStatusCode status, String code, String message, Set<Class<? extends Exception>> exceptions) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.exceptions = exceptions;
    }

    ErrorCode(HttpStatusCode status, String message, Set<Class<? extends Exception>> exceptions) {
        this.status = status;
        this.code = String.valueOf(status.value());
        this.message = message;
        this.exceptions = exceptions;
    }
}
