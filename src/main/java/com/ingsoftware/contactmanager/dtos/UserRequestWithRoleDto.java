package com.ingsoftware.contactmanager.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestWithRoleDto extends UserRequestDto {
    @NotBlank(message = "User role cannot be blank.")
    @Pattern(regexp = "ADMIN|USER",flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Invalid user role.")
    private String role;


}
