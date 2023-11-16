package ddip.me.ddipbe.global.dto;

public record ResponseEnvelope<T>(String code, T data, String message) {
    public static <T> ResponseEnvelope<T> of(T data) {
        return new ResponseEnvelope<>(null, data, null);
    }

    public static <T> ResponseEnvelope<T> of(String code, T data, String message) {
        return new ResponseEnvelope<>(code, data, message);
    }
}
