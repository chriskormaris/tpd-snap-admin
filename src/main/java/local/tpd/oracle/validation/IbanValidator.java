package local.tpd.oracle.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigInteger;

public class IbanValidator implements ConstraintValidator<IbanConstraint, String> {

    @Override
    public void initialize(IbanConstraint iban) {
    }

    @Override
    public boolean isValid(String iban, ConstraintValidatorContext cxt) {
        if (iban.length() != 27) {
            return false;
        }
        if (!iban.startsWith("GR")) {
            return false;
        }
        if (!iban.matches("GR[0-9]{25}")) {
            return false;
        }
        iban = iban.substring(4) + iban.substring(0, 4);
        StringBuilder ibanDigits = new StringBuilder();
        for (int i = 0; i < iban.length(); i++) {
            char lChar = iban.charAt(i);
            if (Character.isDigit(lChar)) {
                ibanDigits.append(lChar);
            } else {
                ibanDigits.append((int) lChar - 55);
            }
        }
        return (new BigInteger(ibanDigits.toString())).mod(BigInteger.valueOf(97)).equals(BigInteger.ONE);
    }

}
