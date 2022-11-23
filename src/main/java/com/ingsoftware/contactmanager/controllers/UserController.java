package com.ingsoftware.contactmanager.controllers;

import com.ingsoftware.contactmanager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private UserService userService;

    @Autowired
    UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/api/hello")
    public ResponseEntity<?> HelloWorld(){
        return ResponseEntity.status(HttpStatus.OK).body("HelloWorld!");
    }

}
