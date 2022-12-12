package com.ingsoftware.contactmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.UpdateTimestamp;


import javax.persistence.*;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "contacts")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contact {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(max = 30, message = "First name must not exceed 30 characters.")
    @Column(name = "first_name")
    private String firstName;

    @Size(max = 30, message = "Last name must not exceed 30 characters.")
    @Column(name = "last_name")
    private String lastName;

    @Size(max = 124, message = "Email must not exceed 124 characters.")
    @Column(name = "email")
    private String email;

    @Size(max = 25, message = "Phone number must not exceed 25 characters.")
    @Column(name = "phone_number")
    private String phoneNumber;

    @Size(max = 100, message = "Address must not exceed 100 characters.")
    @Column(name = "address")
    private String address;

    @Size(max = 100, message = "Info must not exceed 100 characters.")
    @Column(name = "info")
    private String info;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private ContactType contactType;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

    @Generated(GenerationTime.INSERT)
    @Column(name = "guid")
    private UUID guid;
}
