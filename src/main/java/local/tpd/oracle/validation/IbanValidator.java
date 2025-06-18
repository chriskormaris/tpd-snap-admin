package local.tpd.oracle.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IbanValidator implements ConstraintValidator<IbanConstraint, String> {

    @Override
    public void initialize(IbanConstraint iban) {
    }

    @Override
    public boolean isValid(String iban, ConstraintValidatorContext cxt) {
        if (iban.length() != 27) {
            return false;
        }
        return iban.startsWith("GR");
    }

}
