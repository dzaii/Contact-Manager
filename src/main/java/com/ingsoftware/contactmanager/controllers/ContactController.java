package com.ingsoftware.contactmanager.controllers;

import com.ingsoftware.contactmanager.dtos.ContactRequestDto;
import com.ingsoftware.contactmanager.services.ContactService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;


import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.util.UUID;

@RestController
@RequestMapping("/contacts")
@AllArgsConstructor
public class ContactController {

    private ContactService contactService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(contactService.getAll());
    }

    @GetMapping()
    public ResponseEntity<?> getAllUserContacts(@CurrentSecurityContext(expression="authentication.name") String email,
                                                @RequestParam(required = false) String search) {
        return ResponseEntity.status(HttpStatus.OK).body(contactService.getAllByUser(email,search));
    }

    @GetMapping("/{contactGuid}")
    public ResponseEntity<?> getContact(@CurrentSecurityContext(expression="authentication.name") String email,
                                        @PathVariable("contactGuid") UUID contactGuid)
            throws AccessDeniedException, EntityNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(contactService.getByGuid(email,contactGuid));
    }

    @PostMapping()
    public ResponseEntity<?> createContact(@CurrentSecurityContext(expression="authentication.name") String email,
                                           @RequestBody @Valid ContactRequestDto contactRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(contactService.create(email, contactRequestDto));
    }

    @DeleteMapping("/{contactGuid}")
    public ResponseEntity<?> deleteContact(@CurrentSecurityContext(expression="authentication.name") String email,
                                           @PathVariable("contactGuid") UUID contactGuid)
            throws EntityNotFoundException, AccessDeniedException {

        contactService.delete(email, contactGuid);

        return ResponseEntity.status(HttpStatus.OK).body("Deleted.");
    }

    @PutMapping("/{contactGuid}")
    public ResponseEntity<?> editContact(@CurrentSecurityContext(expression="authentication.name") String email,
                                         @PathVariable("contactGuid") UUID contactGuid,
                                         @RequestBody ContactRequestDto contactRequestDto)
            throws EntityNotFoundException, AccessDeniedException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(contactService.edit(email, contactGuid, contactRequestDto));
    }

}
