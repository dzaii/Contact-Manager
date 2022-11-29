package com.ingsoftware.contactmanager.dtos;

import lombok.Data;

import javax.validation.constraints.AssertTrue;

@Data
public class ContactRequestDto {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String address;

    private String type;

    private String info;

    @AssertTrue(message = "One must be present.")
    private boolean isValid() {
        return firstName != null || lastName != null ||
                email != null || phoneNumber != null;
    }
}
