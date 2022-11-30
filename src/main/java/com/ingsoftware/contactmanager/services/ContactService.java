package com.ingsoftware.contactmanager.services;

import com.ingsoftware.contactmanager.dtos.ContactRequestDto;
import com.ingsoftware.contactmanager.dtos.ContactResponseDto;
import com.ingsoftware.contactmanager.mappers.ContactMapper;
import com.ingsoftware.contactmanager.models.Contact;
import com.ingsoftware.contactmanager.models.User;
import com.ingsoftware.contactmanager.repositories.ContactRepository;

import com.ingsoftware.contactmanager.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class ContactService {


    private ContactRepository contactRepository;
    private ContactMapper contactMapper;
    private UserRepository userRepository;


    public List<ContactResponseDto> getAll() {
        return contactMapper.entityToResponse(contactRepository.findAll());
    }

    public List<ContactResponseDto> getAllByUser(String email){

        User user = userRepository.findByEmail(email).get();

        return contactMapper.entityToResponse(contactRepository.findByUser(user));
    }

    public ContactResponseDto create(String email, ContactRequestDto contactRequestDto){

        User user = userRepository.findByEmail(email).get();
        Contact contact = contactMapper.requestToEntity(contactRequestDto);
        contact.setUser(user);

        return contactMapper.entityToResponse(contactRepository.save(contact));


    }

    public String delete(String email, UUID contactGuid) throws InstanceNotFoundException {
        User user = userRepository.findByEmail(email).get();
        Optional<Contact> contactOptional = contactRepository.findByGuid(contactGuid);

        if (contactOptional.isPresent()) {
            Contact contact = contactOptional.get();

            if (user.getId() == contact.getUser().getId()) {
                contactRepository.delete(contact);
                return "Deleted contact.";
            }

        }
        throw new InstanceNotFoundException("Contact not found.");
    }

    public ContactResponseDto editContact(String email, UUID contactGuid, ContactRequestDto contactRequestDto)
            throws InstanceNotFoundException {
        User user = userRepository.findByEmail(email).get();
        Optional<Contact> contactOptional = contactRepository.findByGuid(contactGuid);

        if (contactOptional.isPresent()) {

            Contact contact = contactOptional.get();

            if (user.getId() == contact.getUser().getId()) {
                contact.setFirstName(contactRequestDto.getFirstName());
                contact.setLastName(contactRequestDto.getLastName());
                contact.setEmail(contactRequestDto.getEmail());
                contact.setPhoneNumber(contactRequestDto.getPhoneNumber());
                contact.setAddress(contactRequestDto.getAddress());
//                contact.setContactType();
                contact.setInfo(contactRequestDto.getInfo());

                return contactMapper.entityToResponse(contactRepository.save(contact));
            }

        }
        throw new InstanceNotFoundException("Contact not found.");
    }

}
