package ddip.me.ddipbe.presentation.dto.response;

import ddip.me.ddipbe.domain.SuccessRecord;
import lombok.Getter;

import java.util.Map;

@Getter
public class SuccessRecordPageRes {
    private final Long successRecordId;
    private final String token;
    private final Long eventId;
    private final Map<String, String> successInputInfo;

    public SuccessRecordPageRes(SuccessRecord successRecord) {
        this.successRecordId = successRecord.getId();
        this.token = successRecord.getToken();
        this.eventId = successRecord.getEvent().getId();
        this.successInputInfo = successRecord.getSuccessInputInfo();
    }
}
