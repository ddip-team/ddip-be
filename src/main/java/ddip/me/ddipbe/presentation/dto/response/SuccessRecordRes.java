package ddip.me.ddipbe.presentation.dto.response;

import ddip.me.ddipbe.domain.SuccessRecord;

import java.time.ZonedDateTime;

public record SuccessRecordRes(String token, ZonedDateTime timestamp, boolean isFormInputValueRegistered) {
    public SuccessRecordRes(SuccessRecord successRecord) {
        this(successRecord.getToken(),
                successRecord.getTimestamp(),
                successRecord.getFormInputValue() != null
        );
    }
}
