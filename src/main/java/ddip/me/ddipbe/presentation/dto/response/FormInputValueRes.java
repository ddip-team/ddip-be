package ddip.me.ddipbe.presentation.dto.response;

import ddip.me.ddipbe.domain.SuccessRecord;
import lombok.Getter;

import java.util.Map;

@Getter
public class FormInputValueRes {
    private final Map<String, Object> formInputValue;

    public FormInputValueRes(SuccessRecord successRecord) {
        this.formInputValue = successRecord.getFormInputValue();
    }
}
