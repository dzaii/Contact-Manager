package com.ingsoftware.contactmanager.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserResponseDto {


    private String firstName;

    private String lastName;

    private String email;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private UUID guid;
}
