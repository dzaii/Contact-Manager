package com.ingsoftware.contactmanager.controllers;

import com.ingsoftware.contactmanager.dtos.ContactRequestDto;
import com.ingsoftware.contactmanager.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;
import javax.validation.Valid;
import java.util.UUID;

@RestController
public class ContactController {

    private ContactService contactService;

    @Autowired ContactController(ContactService contactService) {
        this.contactService = contactService;
    }


    @GetMapping("/contacts")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(contactService.getAll());
    }

    @GetMapping("/users/contacts")
    public ResponseEntity<?> getAllUserContacts(@CurrentSecurityContext(expression="authentication.name")
                                                    String email)
            throws InstanceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(contactService.getAllByUser(email));
    }

    @PostMapping("/users/contacts")
    public ResponseEntity<?> createContact(@CurrentSecurityContext(expression="authentication.name")
                                               String email,
                                           @RequestBody @Valid ContactRequestDto contactRequestDto)
            throws InstanceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(contactService.create(email, contactRequestDto));
    }

    @DeleteMapping("/users/contacts/{contactGuid}")
    public ResponseEntity<?> deleteContact(@CurrentSecurityContext(expression="authentication.name")
                                               String email,
                                           @PathVariable("contactGuid") UUID contactGuid)
            throws InstanceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(contactService.delete(email, contactGuid));
    }

    @PutMapping("/users/contacts/{contactGuid}")
    public ResponseEntity<?> editContact(@CurrentSecurityContext(expression="authentication.name")
                                             String email,
                                         @PathVariable("contactGuid") UUID contactGuid,
                                         @RequestBody ContactRequestDto contactRequestDto)
            throws InstanceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(contactService.editContact(email, contactGuid, contactRequestDto));
    }

}
