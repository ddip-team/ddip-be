package ddip.me.ddipbe.presentation.dto.response;

import ddip.me.ddipbe.domain.SuccessRecord;

import java.util.Map;

public record FormInputValueRes(Map<String, Object> formInputValue) {
    public FormInputValueRes(SuccessRecord successRecord) {
        this(successRecord.getFormInputValue());
    }
}
