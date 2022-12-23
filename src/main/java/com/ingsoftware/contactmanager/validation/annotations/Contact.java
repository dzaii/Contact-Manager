package com.ingsoftware.contactmanager.validation.annotations;


import com.ingsoftware.contactmanager.validation.ContactValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;

@Constraint(validatedBy = ContactValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(TYPE)
public @interface Contact {

    String message() default "At least one primary attribute must be present.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
