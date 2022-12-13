package com.ingsoftware.contactmanager.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@NoArgsConstructor
public class ContactResponseDto {

    private UUID guid;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String address;

    private String type;

    private String info;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
