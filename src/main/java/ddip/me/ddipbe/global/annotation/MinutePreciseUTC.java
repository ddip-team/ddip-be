package ddip.me.ddipbe.global.annotation;

import ddip.me.ddipbe.global.validator.MinutePreciseUTCValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = MinutePreciseUTCValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface MinutePreciseUTC {
    String message() default "The datetime must be in UTC and minute-precise";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
