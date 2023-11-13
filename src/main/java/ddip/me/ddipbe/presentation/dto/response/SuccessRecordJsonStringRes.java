package ddip.me.ddipbe.presentation.dto.response;

import ddip.me.ddipbe.domain.SuccessRecord;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
public class SuccessRecordJsonStringRes {
    private Long eventId;
    private Long successRecordId;
    private UUID evnetUuid;
    private Map<String,String> jsonString;

    public SuccessRecordJsonStringRes(SuccessRecord successRecord) {
        this.successRecordId = successRecord.getId();
        this.eventId = successRecord.getEvent().getId();
        this.evnetUuid = successRecord.getEvent().getUuid();
        this.jsonString = successRecord.getJsonString();
    }
}
