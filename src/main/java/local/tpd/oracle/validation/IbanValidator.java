package local.tpd.oracle.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class IbanValidator implements ConstraintValidator<IbanConstraint, String> {

    private static final Map<String, Integer> CONNECTED_COUNTRIES = new HashMap<>();

    static {
        CONNECTED_COUNTRIES.put("AT", 20);  // Austria
        CONNECTED_COUNTRIES.put("BE", 16);  // Belgium
        CONNECTED_COUNTRIES.put("BG", 22);  // Bulgaria
        CONNECTED_COUNTRIES.put("CH", 21);  // Switzerland
        CONNECTED_COUNTRIES.put("CY", 28);  // Cyprus
        CONNECTED_COUNTRIES.put("CZ", 24);  // Czech Republic
        CONNECTED_COUNTRIES.put("DE", 22);  // Germany
        CONNECTED_COUNTRIES.put("DK", 18);  // Denmark
        CONNECTED_COUNTRIES.put("EE", 20);  // Estonia
        CONNECTED_COUNTRIES.put("ES", 24);  // Spain
        CONNECTED_COUNTRIES.put("FI", 18);  // Finland
        CONNECTED_COUNTRIES.put("FR", 27);  // France
        CONNECTED_COUNTRIES.put("GB", 22);  // United Kingdom
        CONNECTED_COUNTRIES.put("GR", 27);  // Greece
        CONNECTED_COUNTRIES.put("HR", 21);  // Croatia
        CONNECTED_COUNTRIES.put("HU", 28);  // Hungary
        CONNECTED_COUNTRIES.put("IE", 22);  // Ireland
        CONNECTED_COUNTRIES.put("IS", 26);  // Iceland
        CONNECTED_COUNTRIES.put("IT", 27);  // Italy
        CONNECTED_COUNTRIES.put("LI", 21);  // Liechtenstein
        CONNECTED_COUNTRIES.put("LT", 20);  // Lithuania
        CONNECTED_COUNTRIES.put("LU", 20);  // Luxembourg
        CONNECTED_COUNTRIES.put("LV", 21);  // Latvia
        CONNECTED_COUNTRIES.put("MC", 27);  // Monaco
        CONNECTED_COUNTRIES.put("MT", 31);  // Malta
        CONNECTED_COUNTRIES.put("NL", 18);  // Netherlands
        CONNECTED_COUNTRIES.put("NO", 15);  // Norway
        CONNECTED_COUNTRIES.put("PL", 28);  // Poland
        CONNECTED_COUNTRIES.put("PT", 25);  // Portugal
        CONNECTED_COUNTRIES.put("RO", 25);  // Romania
        CONNECTED_COUNTRIES.put("SE", 24);  // Sweden
        CONNECTED_COUNTRIES.put("SI", 19);  // Slovenia
        CONNECTED_COUNTRIES.put("SK", 24);  // Slovak Republic
        CONNECTED_COUNTRIES.put("SM", 27);  // San Marino
        // CONNECTED_COUNTRIES.put("ZZ", 14);  // OTHER
    }

    @Override
    public void initialize(IbanConstraint iban) {
    }

    @Override
    public boolean isValid(String iban, ConstraintValidatorContext cxt) {
        String countryCode = iban.substring(0, 2);
        Integer ibanLength = CONNECTED_COUNTRIES.get(countryCode);
        if (ibanLength == null) {
            return false;
        }
        if (iban.length() != ibanLength) {
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
