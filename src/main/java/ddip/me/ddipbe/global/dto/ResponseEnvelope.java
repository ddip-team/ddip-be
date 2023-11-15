package ddip.me.ddipbe.global.dto;

import lombok.Getter;

@Getter
public class ResponseEnvelope<T> {
    private final String code;
    private final T data;
    private final String message;

    public ResponseEnvelope(T data) {
        this.code = null;
        this.data = data;
        this.message = null;
    }

    public ResponseEnvelope(String code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }
}
