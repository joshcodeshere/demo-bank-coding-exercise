package com.example.bank.validation;

import com.example.bank.domain.Customer;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

/**
 * Simple validation logic for a {@link Customer} SSN number.
 * 
 * Referenced from https://en.wikipedia.org/wiki/Social_Security_number "Valid
 * SSNs".
 */
@Slf4j
public class ValidSsnImpl implements
        ConstraintValidator<ValidSsn, Long> {

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) {
            updateMessage("SSN is null", context);
            return false;
        }

        final String ssnString = value.toString();
        if (ssnString.length() != 9) {
            updateMessage("Invalid SSN length", context);
            return false;
        }

        final String areaNumber = ssnString.substring(0, 3);
        if ("000".equals(areaNumber)
                || areaNumber.startsWith("666")
                || areaNumber.startsWith("9")) {
            updateMessage("Invalid SSN area number", context);
            return false;
        }

        final String groupNumber = ssnString.substring(3, 5);
        if ("00".equals(groupNumber)) {
            updateMessage("Invalid SSN group number", context);
            return false;
        }

        final String serialNumber = ssnString.substring(5, 9);
        if ("0000".equals(serialNumber)) {
            updateMessage("Invalid SSN serial number", context);
            return false;
        }

        return true;
    }

    private void updateMessage(String message, ConstraintValidatorContext context) {
        log.debug("SSN validation failed: " + message);
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }

}
