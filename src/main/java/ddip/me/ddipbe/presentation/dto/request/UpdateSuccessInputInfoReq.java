package ddip.me.ddipbe.presentation.dto.request;

import lombok.Getter;

import java.util.Map;

@Getter
public class UpdateSuccessInputInfoReq {
    private final Map<String, String> successInputInfo;

    public UpdateSuccessInputInfoReq(Map<String, String> successInputInfo) {
        this.successInputInfo = successInputInfo;
    }
}
