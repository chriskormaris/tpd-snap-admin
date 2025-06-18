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
@Constraint(validatedBy = AfmValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface AfmConstraint {

    String message() default "το ΑΦΜ δεν είναι έγκυρο";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
