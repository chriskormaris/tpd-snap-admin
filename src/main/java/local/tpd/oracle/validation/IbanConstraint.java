package local.tpd.oracle.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IbanValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface IbanConstraint {

    String message() default "το IBAN δεν είναι έγκυρο";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
