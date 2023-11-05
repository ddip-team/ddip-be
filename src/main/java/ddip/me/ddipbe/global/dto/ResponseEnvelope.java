package ddip.me.ddipbe.global.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseEnvelope<T> {
    private final String code;
    private final T data;
    private String message;

    public ResponseEnvelope(String code, T data) {
        this.code = code;
        this.data = data;
        this.message=null;
    }

    public ResponseEnvelope(String code) {
        this.code = code;
        this.data=null;
        this.message=null;
    }

    public ResponseEnvelope(String code, String message) {
        this.code = code;
        this.data=null;
        this.message = message;
    }

}
