package ddip.me.ddipbe.application.dto;

import ddip.me.ddipbe.domain.SuccessRecord;

import java.time.ZonedDateTime;

public record SuccessRecordDto(String token, ZonedDateTime createdAt, boolean isFormInputValueRegistered) {
    public SuccessRecordDto(SuccessRecord successRecord) {
        this(successRecord.getToken(),
                successRecord.getCreatedAt(),
                successRecord.getFormInputValue() != null
        );
    }
}
