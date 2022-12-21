package com.ingsoftware.contactmanager.controllers;

import com.ingsoftware.contactmanager.dtos.UserRequestDto;
import com.ingsoftware.contactmanager.services.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/register")
@AllArgsConstructor
public class RegistrationController {

    private RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody @Valid UserRequestDto userRequestDto){
        return ResponseEntity.status(HttpStatus.OK).body(registrationService.register(userRequestDto));
    }

    @PostMapping("/{guid}")
    public ResponseEntity<?> verify(@RequestParam String code, @PathVariable("guid") UUID guid){
        registrationService.verify(code,guid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
