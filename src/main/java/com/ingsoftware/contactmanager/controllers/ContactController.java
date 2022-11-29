package com.ingsoftware.contactmanager.controllers;

import com.ingsoftware.contactmanager.dtos.ContactRequestDto;
import com.ingsoftware.contactmanager.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/users/{userGuid}/contacts")
    public ResponseEntity<?> getAllUserContacts(@PathVariable("userGuid") UUID userGuid)
            throws InstanceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(contactService.getAllByUserGuid(userGuid));
    }

    @PostMapping("/users/{userGuid}/contacts")
    public ResponseEntity<?> createContact(@PathVariable("userGuid") UUID userGuid,
                                           @RequestBody @Valid ContactRequestDto contactRequestDto)
            throws InstanceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(contactService.create(userGuid, contactRequestDto));
    }

    @DeleteMapping("/users/{userGuid}/contacts/{contactGuid}")
    public ResponseEntity<?> deleteContact(@PathVariable("userGuid") UUID userGuid,
                                           @PathVariable("contactGuid") UUID contactGuid)
            throws InstanceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(contactService.delete(userGuid, contactGuid));
    }

    @PutMapping("/users/{userGuid}/contacts/{contactGuid}")
    public ResponseEntity<?> editContact(@PathVariable("userGuid") UUID userGuid,
                                         @PathVariable("contactGuid") UUID contactGuid,
                                         @RequestBody ContactRequestDto contactRequestDto)
            throws InstanceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(contactService.editContact(userGuid, contactGuid, contactRequestDto));
    }

}
