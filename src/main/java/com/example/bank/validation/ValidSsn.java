package com.example.bank.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * SSN validation annotation.
 * 
 * @see {@link ValidSsnImpl}
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidSsnImpl.class)
public @interface ValidSsn {

    String message() default "Invalid SSN";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
