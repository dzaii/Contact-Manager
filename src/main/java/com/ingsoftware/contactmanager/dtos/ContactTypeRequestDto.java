package com.ingsoftware.contactmanager.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data public class ContactTypeRequestDto {

    private int typeId;
    @NotBlank(message = "Contact type must not be empty.")
    @NotNull(message = "Contact type must not be empty.")
    private String value;
}
