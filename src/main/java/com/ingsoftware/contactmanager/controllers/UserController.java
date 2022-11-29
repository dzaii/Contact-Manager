package com.ingsoftware.contactmanager.controllers;

import com.ingsoftware.contactmanager.dtos.UserRequestDto;
import com.ingsoftware.contactmanager.dtos.UserResponseDto;
import com.ingsoftware.contactmanager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceNotFoundException;
import javax.validation.Valid;
import java.util.UUID;

@RestController @RequestMapping("/users") public class UserController {
    private final UserService userService;

    @Autowired UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all") public ResponseEntity<?> getAll() {

        return ResponseEntity.status(HttpStatus.OK).body(userService.getAll());
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody @Valid UserRequestDto userRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.registerUser(userRequestDto));
    }

    @DeleteMapping("/{guid}") public ResponseEntity<?> deleteUser(@PathVariable("guid") UUID guid)
            throws InstanceNotFoundException {
        userService.deleteUser(guid);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted user.");
    }

    @GetMapping("/{guid}") public ResponseEntity<?> getUser(@PathVariable("guid") UUID guid)
            throws InstanceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getByGuid(guid));
    }

    @PutMapping("/{guid}") public ResponseEntity<?> editUser(@RequestBody @Valid UserRequestDto userRequestDto,
                                                             @PathVariable("guid") UUID guid)
            throws InstanceNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.editUser(userRequestDto, guid));
    }
}
