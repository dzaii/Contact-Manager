package com.ingsoftware.contactmanager.services;

import com.ingsoftware.contactmanager.models.ContactType;
import com.ingsoftware.contactmanager.models.User;
import com.ingsoftware.contactmanager.repositories.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public void save(User user){
        this.userRepository.save(user);
    }
}
