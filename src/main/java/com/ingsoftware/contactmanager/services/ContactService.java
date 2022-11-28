package com.ingsoftware.contactmanager.services;

import com.ingsoftware.contactmanager.models.Contact;
import com.ingsoftware.contactmanager.repositories.ContactRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ContactService {


    private ContactRepository contactRepository;
    private ContactMapper contactMapper;


    public void save(Contact contact){
        contactRepository.save(contact);
    }

    public List<ContactResponseDto> getAll(){
        return contactMapper.contactToContactResponse(contactRepository.findAll());
    }


}
