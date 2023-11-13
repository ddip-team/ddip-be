package ddip.me.ddipbe.presentation.dto.request;

import lombok.Getter;

import java.util.Map;

@Getter
public class CreateJsonStringReq {
    private final Map<String, String> jsonString;

    public CreateJsonStringReq(Map<String, String> jsonString) {
        this.jsonString = jsonString;
    }
}
