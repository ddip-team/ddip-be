package ddip.me.ddipbe.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record RegisterFormInputValueReq(@NotNull Map<String, Object> formInputValue) {
}
