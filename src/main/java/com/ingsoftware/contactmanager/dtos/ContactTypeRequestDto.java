package com.ingsoftware.contactmanager.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ContactTypeRequestDto {

    @Size(max = 20, message = "Contact type must not exceed 30 characters.")
    @NotBlank(message = "Contact type must not be empty.")
    private String value;
}
