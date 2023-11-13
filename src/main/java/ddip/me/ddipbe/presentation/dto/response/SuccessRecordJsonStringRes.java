package ddip.me.ddipbe.presentation.dto.response;

import ddip.me.ddipbe.domain.SuccessRecord;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
public class SuccessRecordJsonStringRes {
    private Long successRecordId;
    private Map<String,String> jsonString;

    public SuccessRecordJsonStringRes(SuccessRecord successRecord) {
        this.successRecordId = successRecord.getId();
        this.jsonString = successRecord.getJsonString();
    }
}
