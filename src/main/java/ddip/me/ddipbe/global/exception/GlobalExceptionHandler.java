package ddip.me.ddipbe.global.exception;

import ddip.me.ddipbe.global.dto.ResponseEnvelope;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Map<Class<? extends Exception>, ErrorCode> exceptionMap;

    public GlobalExceptionHandler() {
        Map<Class<? extends Exception>, ErrorCode> tempMap = new HashMap<>();
        for (ErrorCode errorCode : ErrorCode.values()) {
            Set<Class<? extends Exception>> exceptions = errorCode.getExceptions();
            for (Class<? extends Exception> exception : exceptions) {
                tempMap.put(exception, errorCode);
            }
        }

        this.exceptionMap = Collections.unmodifiableMap(tempMap);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseEnvelope<?>> handleException(Exception e) {
        if (exceptionMap.containsKey(e.getClass())) {
            ErrorCode errorCode = exceptionMap.get(e.getClass());
            Object data = parseErrorData(e);
            ResponseEnvelope<?> res = new ResponseEnvelope<>(errorCode.getCode(), data, errorCode.getMessage());
            return ResponseEntity.status(errorCode.getStatus()).body(res);
        }

        // 예상 못한 에러
        log.error("Unexpected error occurred", e);
        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus())
                .body(new ResponseEnvelope<>(
                        ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                        null,
                        ErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
    }

    private Object parseErrorData(Exception e) {
        return switch (e.getClass().getSimpleName()) {
            case "MethodArgumentNotValidException" ->
                    dataForMethodArgumentNotValidException((MethodArgumentNotValidException) e);
            case "ConstraintViolationException" ->
                    dataForConstraintViolationException((ConstraintViolationException) e);
            default -> null;
        };
    }

    private Object dataForMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return e.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldError(error.getField(), error.getDefaultMessage()))
                .toList();
    }

    private Object dataForConstraintViolationException(ConstraintViolationException e) {
        return e.getConstraintViolations().stream()
                .map(violation -> new FieldError(violation.getPropertyPath().toString(), violation.getMessage()))
                .toList();
    }

    private record FieldError(String field, String message) {
    }
}
