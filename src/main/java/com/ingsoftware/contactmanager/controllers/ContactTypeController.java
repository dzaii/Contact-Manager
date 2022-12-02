package com.ingsoftware.contactmanager.controllers;

import com.ingsoftware.contactmanager.dtos.ContactTypeRequestDto;
import com.ingsoftware.contactmanager.services.ContactTypeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.directory.AttributeInUseException;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("contacts/types")
@AllArgsConstructor
public class ContactTypeController {

    ContactTypeService contactTypeService;

    @GetMapping() public ResponseEntity<?> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(contactTypeService.getAll());
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody @Valid ContactTypeRequestDto contactTypeRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(contactTypeService.create(contactTypeRequestDto));
    }

    @DeleteMapping("/{guid}") public ResponseEntity<?> delete(@PathVariable("guid") UUID guid)
            throws EntityNotFoundException, AttributeInUseException {
        contactTypeService.delete(guid);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted.");
    }

    @PutMapping("/{guid}") public ResponseEntity<?> edit(@RequestBody @Valid ContactTypeRequestDto contactTypeRequestDto,
                                                         @PathVariable("guid") UUID guid)
            throws EntityNotFoundException{
        return ResponseEntity.status(HttpStatus.OK).body(contactTypeService.edit(contactTypeRequestDto,guid));
    }

}
