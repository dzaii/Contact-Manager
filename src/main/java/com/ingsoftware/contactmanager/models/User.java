package com.ingsoftware.contactmanager.models;

import com.ingsoftware.contactmanager.models.enums.UserRole;

import lombok.Data;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "first_name")
    @Size(max = 30, message = "First name must not exceed 30 characters.")
    private String firstName;

    @Size(max = 30, message = "Last name must not exceed 30 characters.")
    @Column(name = "last_name")
    private String lastName;

    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE, message = "Invalid email format.")
    @NotBlank
    @Size(max = 124, message = "Email must not exceed 124 characters.")
    @Column(name = "email")
    private String email;

    @Column(name = "password")
    @NotBlank
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
            message = "Password must contain at least: 8 characters, one upper Case letter," +
                    " one lower case letter, one number and one special character.")
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @OneToMany(mappedBy="user", cascade = CascadeType.ALL)
    private List<Contact> contactList = new ArrayList<>();

    @Generated(GenerationTime.INSERT)
    @Column(name = "guid")
    private UUID guid;
}
