package ddip.me.ddipbe.presentation.dto.response;

import ddip.me.ddipbe.domain.SuccessRecord;
import lombok.Getter;

import java.util.Map;

@Getter
public class SuccessRecordSuccessInputInfoRes {
    private final Long successRecordId;
    private final Map<String,String> successInputInfo;

    public SuccessRecordSuccessInputInfoRes(SuccessRecord successRecord) {
        this.successRecordId = successRecord.getId();
        this.successInputInfo = successRecord.getSuccessInputInfo();
    }
}
