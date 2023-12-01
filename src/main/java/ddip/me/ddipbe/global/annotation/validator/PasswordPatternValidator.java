package ddip.me.ddipbe.global.annotation.validator;

import ddip.me.ddipbe.global.annotation.PasswordPattern;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordPatternValidator implements ConstraintValidator<PasswordPattern, String> {
    private static final String UUID_REGEX = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,20}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.matches(UUID_REGEX);
    }
}
