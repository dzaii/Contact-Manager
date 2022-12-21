package com.ingsoftware.contactmanager.controllers;

import com.ingsoftware.contactmanager.dtos.ContactRequestDto;
import com.ingsoftware.contactmanager.dtos.ContactResponseDto;
import com.ingsoftware.contactmanager.exeptionHandlers.ErrorDetails;
import com.ingsoftware.contactmanager.services.ContactService;
import com.ingsoftware.contactmanager.swagger.ApiPageable;
import lombok.AllArgsConstructor;
import org.apache.el.parser.ParseException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;


import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.UUID;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping("/contacts")
@AllArgsConstructor
@Validated
public class ContactController {

    private ContactService contactService;


    @ApiPageable
    @GetMapping("/all")
    public ResponseEntity<?> getAll(@RequestParam(required = false)
                                    @Size(min = 3, message = "Search requires at least 3 characters.")
                                    String search, @ApiIgnore Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(contactService.getAll(search,pageable));
    }

    @ApiPageable
    @GetMapping()
    public ResponseEntity<?> getAllUserContacts(
            @ApiIgnore @CurrentSecurityContext(expression="authentication.name") String email,
            @RequestParam(required = false) @Size(min = 3, message = "Search requires at least 3 characters.")
            String search,@ApiIgnore Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(contactService.getAllByUser(email,search, pageable));
    }

    @GetMapping("/{contactGuid}")
    public ResponseEntity<ContactResponseDto> getContact(
            @ApiIgnore @CurrentSecurityContext(expression="authentication.name") String email,
            @PathVariable("contactGuid") UUID contactGuid)
            throws AccessDeniedException, EntityNotFoundException {

        return ResponseEntity.status(HttpStatus.OK).body(contactService.getByGuid(email,contactGuid));
    }

    @PostMapping()
    public ResponseEntity<ContactResponseDto> createContact(
            @ApiIgnore @CurrentSecurityContext(expression="authentication.name") String email,
            @RequestBody @Valid ContactRequestDto contactRequestDto) {

        return ResponseEntity.status(HttpStatus.OK).body(contactService.create(email, contactRequestDto));
    }

    @PostMapping("/upload")
    public ResponseEntity<ErrorDetails> uploadContacts(
            @ApiIgnore @CurrentSecurityContext(expression="authentication.name") String email,
            @RequestParam MultipartFile file) throws DataFormatException, ParseException, IOException {

        return ResponseEntity.status(HttpStatus.OK).body(contactService.uploadContactsFromCSV(email,file));
    }

    @DeleteMapping("/{contactGuid}")
    public ResponseEntity<?> deleteContact(
            @ApiIgnore @CurrentSecurityContext(expression="authentication.name") String email,
            @PathVariable("contactGuid") UUID contactGuid) throws EntityNotFoundException, AccessDeniedException {

        contactService.delete(email, contactGuid);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{contactGuid}")
    public ResponseEntity<ContactResponseDto> editContact(
             @ApiIgnore @CurrentSecurityContext(expression="authentication.name") String email,
             @PathVariable("contactGuid") UUID contactGuid,
             @RequestBody ContactRequestDto contactRequestDto) throws EntityNotFoundException, AccessDeniedException {

        return ResponseEntity.status(HttpStatus.OK)
                .body(contactService.edit(email, contactGuid, contactRequestDto));
    }

}
