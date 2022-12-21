package com.ingsoftware.contactmanager.controllers;

import com.ingsoftware.contactmanager.dtos.UserRequestDto;
import com.ingsoftware.contactmanager.dtos.UserRequestWithRoleDto;
import com.ingsoftware.contactmanager.dtos.UserResponseDto;
import com.ingsoftware.contactmanager.services.UserService;
import com.ingsoftware.contactmanager.swagger.ApiPageable;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiPageable
    @GetMapping()
    public ResponseEntity<?> getAll(@ApiIgnore Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(userService.getAll(pageable));
    }

    @PostMapping()
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserRequestWithRoleDto userRequestWithRoleDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.create(userRequestWithRoleDto));
    }

    @DeleteMapping("/{guid}")
    public ResponseEntity<?> deleteUser(@PathVariable("guid") UUID guid)
            throws EntityNotFoundException {
        userService.delete(guid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{guid}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable("guid") UUID guid)
            throws EntityNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getByGuid(guid));
    }

    @PutMapping("/{guid}")
    public ResponseEntity<UserResponseDto> editUser(@RequestBody @Valid UserRequestWithRoleDto userRequestWithRoleDto,
                                                    @PathVariable("guid") UUID guid)
            throws EntityNotFoundException, DuplicateKeyException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.edit(userRequestWithRoleDto, guid));
    }

    @GetMapping("/current")
    public ResponseEntity<UserResponseDto> getCurrent(
            @ApiIgnore @CurrentSecurityContext(expression="authentication.name") String email)
            throws EntityNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getCurrent(email));
    }

    @PutMapping("/current")
    public ResponseEntity<UserResponseDto> editCurrent(
            @ApiIgnore @CurrentSecurityContext(expression="authentication.name") String email,
            @RequestBody @Valid UserRequestDto userRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.editCurrent(userRequestDto,email));
    }

    @DeleteMapping("/current")
    public ResponseEntity<UserResponseDto> deleteCurrent(
            @ApiIgnore @CurrentSecurityContext(expression="authentication.name") String email) {
        userService.deleteCurrent(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
