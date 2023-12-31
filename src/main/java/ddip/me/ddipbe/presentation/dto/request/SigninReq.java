package ddip.me.ddipbe.presentation.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SigninReq(@NotBlank @Email String email, @NotBlank String password) {
}
