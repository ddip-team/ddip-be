package ddip.me.ddipbe.presentation.dto.response;

import ddip.me.ddipbe.domain.SuccessRecord;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class SuccessRecordRes {

    private final String token;
    private final ZonedDateTime timestamp;
    private final boolean isFormInputValueRegistered;

    public SuccessRecordRes(SuccessRecord successRecord) {
        this.token = successRecord.getToken();
        this.timestamp = successRecord.getTimestamp();
        this.isFormInputValueRegistered = successRecord.getFormInputValue() != null;
    }
}
