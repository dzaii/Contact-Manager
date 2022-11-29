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

    public List<ContactResponseDto> getAllByUserGuid(UUID userGuid) throws InstanceNotFoundException {
        Optional<User> userOptional = userRepository.findByGuid(userGuid);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return contactMapper.entityToResponse(contactRepository.findByUser(user));
        }
        throw new InstanceNotFoundException("Invalid guid.");
    }

    public ContactResponseDto create(UUID userGuid, ContactRequestDto contactRequestDto)
            throws InstanceNotFoundException {
        Optional<User> userOptional = userRepository.findByGuid(userGuid);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Contact contact = contactMapper.requestToEntity(contactRequestDto);
            contact.setUser(user);
            return contactMapper.entityToResponse(contactRepository.save(contact));
        }
        throw new InstanceNotFoundException("Invalid guid.");
    }

    public String delete(UUID userGuid, UUID contactGuid) throws InstanceNotFoundException {
        Optional<User> userOptional = userRepository.findByGuid(userGuid);
        Optional<Contact> contactOptional = contactRepository.findByGuid(contactGuid);

        if (userOptional.isPresent() && contactOptional.isPresent()) {
            User user = userOptional.get();
            Contact contact = contactOptional.get();

            if (user.getId() == contact.getUser().getId()) {
                contactRepository.delete(contact);
                return "Deleted contact.";
            }

        }
        throw new InstanceNotFoundException("Invalid user/contact guid.");
    }

    public ContactResponseDto editContact(UUID userGuid, UUID contactGuid, ContactRequestDto contactRequestDto)
            throws InstanceNotFoundException {
        Optional<User> userOptional = userRepository.findByGuid(userGuid);
        Optional<Contact> contactOptional = contactRepository.findByGuid(contactGuid);

        if (userOptional.isPresent() && contactOptional.isPresent()) {
            User user = userOptional.get();
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
        throw new InstanceNotFoundException("Invalid user/contact guid.");
    }

}
