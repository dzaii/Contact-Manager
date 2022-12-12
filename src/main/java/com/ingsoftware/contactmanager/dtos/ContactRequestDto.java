package com.ingsoftware.contactmanager.dtos;

import com.ingsoftware.contactmanager.validation.annotations.Contact;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@Builder
@Contact(message = "At least one primary attribute must be present.")
public class ContactRequestDto {

    @Size(max = 30, message = "First name must not exceed 30 characters.")
    private String firstName;

    @Size(max = 30, message = "Last name must not exceed 30 characters.")
    private String lastName;

    @Size(max = 124, message = "Email must not exceed 124 characters.")
    private String email;

    @Size(max = 25, message = "Phone number must not exceed 25 characters.")
    private String phoneNumber;

    @Size(max = 100, message = "Address must not exceed 100 characters.")
    private String address;

    @Size(max = 20, message = "Contact type must not exceed 20 characters.")
    private String type;

    @Size(max = 100, message = "Info must not exceed 100 characters.")
    private String info;

}
