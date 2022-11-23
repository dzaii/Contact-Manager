package com.ingsoftware.contactmanager.models;

import com.ingsoftware.contactmanager.models.enums.ContactType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "contacts")
@NoArgsConstructor
@AllArgsConstructor
public class Contact {

    @Id
    @Column(name = "contact_id")
    @GeneratedValue(generator = "contacts_contact_id_seq")
    private int id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email")
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "modified_at")
    private LocalDateTime modifiedAt;
    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "type_id")
    private ContactType contactType;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
}
