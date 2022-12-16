package com.ingsoftware.contactmanager.services;

import com.ingsoftware.contactmanager.dtos.ContactRequestDto;
import com.ingsoftware.contactmanager.dtos.ContactResponseDto;
import com.ingsoftware.contactmanager.mappers.ContactMapper;
import com.ingsoftware.contactmanager.models.Contact;
import com.ingsoftware.contactmanager.models.ContactType;
import com.ingsoftware.contactmanager.models.User;
import com.ingsoftware.contactmanager.models.enums.UserRole;
import com.ingsoftware.contactmanager.repositories.ContactRepository;

import com.ingsoftware.contactmanager.repositories.ContactTypeRepository;
import com.ingsoftware.contactmanager.repositories.UserRepository;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


import javax.persistence.EntityNotFoundException;
import java.nio.file.AccessDeniedException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ContactService {

    private ContactRepository contactRepository;
    private ContactMapper contactMapper;
    private UserRepository userRepository;
    private ContactTypeRepository contactTypeRepository;

    @Transactional(readOnly = true)
    public Page<ContactResponseDto> getAll(String search, Pageable pageable) {
        if(StringUtils.hasText(search)) {
            return contactRepository.searchAllContacts(search.toLowerCase(), search.toLowerCase(),pageable).map(contactMapper::entityToResponse);
        }
            return contactRepository.findAll(pageable).map(contactMapper::entityToResponse);
    }

    @Transactional(readOnly = true)
    public Page<ContactResponseDto> getAllByUser(String email, String search, Pageable pageable) {

        User user = userRepository.findByEmail(email).get();
        int userId = user.getId();

        if(StringUtils.hasText(search)){
            return contactRepository.searchContactsByUser(userId,search.toLowerCase(),search.toLowerCase(),pageable)
                    .map(contactMapper::entityToResponse);
        }
            return contactRepository.findByUser(user,pageable).map( contactMapper::entityToResponse);
    }

    @Transactional(rollbackFor = Exception.class)
    public ContactResponseDto create(String email, ContactRequestDto contactRequestDto) {

        User user = userRepository.findByEmail(email).get();
        Contact contact = contactMapper.requestToEntity(contactRequestDto);
        contact.setUser(user);

        if (contactRequestDto.getType() != null) {
            ContactType contactType = contactTypeRepository.findByValue(contactRequestDto.getType())
                    .orElseThrow(() -> new EntityNotFoundException("Invalid contact type."));

            contact.setContactType(contactType);
        }
        return contactMapper.entityToResponse(contactRepository.save(contact));
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(String email, UUID contactGuid) throws EntityNotFoundException, AccessDeniedException {

        User user = userRepository.findByEmail(email).get();
        Contact contact = findByGuid(contactGuid);

        if (user.getId() == contact.getUser().getId()) {
            contactRepository.delete(contact);
            return;
        }
        throw new AccessDeniedException("Contact does not belong to current user.");
    }

    @Transactional(rollbackFor = Exception.class)
    public ContactResponseDto edit(String email, UUID contactGuid, ContactRequestDto contactRequestDto)
            throws AccessDeniedException {

        User user = userRepository.findByEmail(email).get();
        Contact contact = findByGuid(contactGuid);

        if (user.getId() == contact.getUser().getId()) {

            if (contactRequestDto.getType() != null) {
                ContactType contactType = contactTypeRepository.findByValue(contactRequestDto.getType())
                        .orElseThrow(() -> new EntityNotFoundException("Invalid contact type."));
                contact.setContactType(contactType);
            }

            contactMapper.updateEntityFromRequest(contact, contactRequestDto);
            return contactMapper.entityToResponse(contactRepository.save(contact));
        }
        throw new AccessDeniedException("Contact does not belong to current user.");
    }

    private Contact findByGuid(UUID guid) throws EntityNotFoundException {
        return contactRepository.findByGuid(guid)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found."));
    }

    @Transactional(readOnly = true)
    public ContactResponseDto getByGuid(String email, UUID contactGuid)
            throws EntityNotFoundException, AccessDeniedException {

        Contact contact = findByGuid(contactGuid);
        User user = userRepository.findByEmail(email).get();

        if ((contact.getUser().getId() == user.getId()) || user.getRole().equals(UserRole.ADMIN)) {
            return contactMapper.entityToResponse(contact);
        }
        throw new AccessDeniedException("Contact does not belong to current user.");
    }
}
