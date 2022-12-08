package com.ingsoftware.contactmanager.validation;

import com.ingsoftware.contactmanager.dtos.ContactRequestDto;
import org.springframework.util.StringUtils;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ContactValidator
        implements
        ConstraintValidator<com.ingsoftware.contactmanager.validation.annotations.Contact, ContactRequestDto> {

    public void initialize(ContactRequestDto contactRequestDto) {
    }

    public boolean isValid(ContactRequestDto contactRequestDto, ConstraintValidatorContext cvc) {
        return StringUtils.hasText(contactRequestDto.getLastName()) || StringUtils.hasText(contactRequestDto.getEmail())
                || StringUtils.hasText(contactRequestDto.getFirstName()) ||
                StringUtils.hasText(contactRequestDto.getPhoneNumber());
    }
}
