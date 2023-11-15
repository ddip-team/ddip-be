package ddip.me.ddipbe.presentation.dto.request;

import lombok.Getter;

import java.util.Map;

@Getter
public class RegisterFormInputValueReq {

    private final Map<String, Object> formInputValue;

    public RegisterFormInputValueReq(Map<String, Object> formInputValue) {
        this.formInputValue = formInputValue;
    }
}
