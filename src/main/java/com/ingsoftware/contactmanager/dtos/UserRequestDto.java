package com.ingsoftware.contactmanager.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class UserRequestDto {

    @Size(max = 30, message = "First name must not exceed 30 characters.")
    private String firstName;

    @Size(max = 30, message = "Last name must not exceed 30 characters.")
    private String lastName;

    @NotBlank
    @Size(max = 124, message = "Email must not exceed 124 characters.")
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE, message = "Invalid email format.")
    private String email;

    @NotBlank
    @Size(max = 30, message = "Password must not exceed 30 characters.")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
            message = "Password must contain at least: 8 characters, one upper Case letter," +
                    " one lower case letter, one number and one special character.")
    private String password;

    @NotBlank(message = "User role cannot be blank.")
    @Pattern(regexp = "ADMIN|USER",flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Invalid user role.")
    private String role;

}
