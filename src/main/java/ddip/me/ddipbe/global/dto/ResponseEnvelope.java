package ddip.me.ddipbe.global.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseEnvelope<T> {
    private final HttpStatus httpStatus;
    private final T data;
    private String message;

    public ResponseEnvelope(HttpStatus httpStatus, T data) {
        this.httpStatus = httpStatus;
        this.data = data;
        this.message = httpStatus.getReasonPhrase();
    }
}
