package com.ingsoftware.contactmanager.services;

import com.ingsoftware.contactmanager.models.Contact;
import com.ingsoftware.contactmanager.repositories.ContactRepository;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    private ContactRepository contactRepository;

    ContactService(ContactRepository contactRepository){
        this.contactRepository = contactRepository;
    }
    public void save(Contact contact){
        contactRepository.save(contact);
    }
}
