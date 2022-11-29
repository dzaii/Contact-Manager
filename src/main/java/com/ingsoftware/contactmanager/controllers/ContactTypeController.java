package com.ingsoftware.contactmanager.controllers;

import com.ingsoftware.contactmanager.dtos.ContactTypeRequestDto;
import com.ingsoftware.contactmanager.services.ContactTypeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;
import javax.validation.Valid;

@RestController @RequestMapping("contacts/types") @AllArgsConstructor public class ContactTypeController {

    ContactTypeService contactTypeService;

    @GetMapping() public ResponseEntity<?> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(contactTypeService.getAll());
    }

    @PostMapping()
    public ResponseEntity<?> createOrUpdate(@RequestBody @Valid ContactTypeRequestDto contactTypeRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(contactTypeService.createOrUpdate(contactTypeRequestDto));
    }

    @DeleteMapping("/{id}") public ResponseEntity<?> delete(@PathVariable("id") int id)
            throws InstanceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(contactTypeService.delete(id));
    }

}
