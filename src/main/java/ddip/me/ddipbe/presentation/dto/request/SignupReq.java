package ddip.me.ddipbe.presentation.dto.request;

import ddip.me.ddipbe.global.annotation.PasswordPattern;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignupReq(
        @NotBlank @Email String email,
        @PasswordPattern @NotBlank String password) {
}
