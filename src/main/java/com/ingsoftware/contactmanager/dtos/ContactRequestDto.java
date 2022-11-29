package com.ingsoftware.contactmanager.dtos;

import lombok.Data;

@Data
public class ContactRequestDto {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String address;

    private String type;

    private String info;
}
