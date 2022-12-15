package com.ingsoftware.contactmanager.services;

import com.ingsoftware.contactmanager.dtos.ContactRequestDto;
import com.ingsoftware.contactmanager.dtos.ContactResponseDto;
import com.ingsoftware.contactmanager.exeptionHandlers.ErrorDetails;
import com.ingsoftware.contactmanager.mappers.CSVMapper;
import com.ingsoftware.contactmanager.mappers.ContactMapper;
import com.ingsoftware.contactmanager.models.Contact;
import com.ingsoftware.contactmanager.models.ContactType;
import com.ingsoftware.contactmanager.models.User;
import com.ingsoftware.contactmanager.models.enums.UserRole;
import com.ingsoftware.contactmanager.repositories.ContactRepository;

import com.ingsoftware.contactmanager.repositories.ContactTypeRepository;
import com.ingsoftware.contactmanager.repositories.UserRepository;
import lombok.AllArgsConstructor;

import org.apache.el.parser.ParseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

@Service
@AllArgsConstructor
public class ContactService {

    private ContactRepository contactRepository;
    private ContactMapper contactMapper;
    private UserRepository userRepository;
    private ContactTypeRepository contactTypeRepository;
    private CSVMapper csvMapper;
    private Validator validator;

    @Transactional(readOnly = true)
    public Page<ContactResponseDto> getAll(String search, Pageable pageable) {
        if(StringUtils.hasText(search)) {
            return contactRepository
                    .searchAllContacts(search.toLowerCase(), search.toLowerCase(),pageable)
                    .map(contactMapper::entityToResponse);
        }
            return contactRepository.findAll(pageable).map(contactMapper::entityToResponse);
    }

    @Transactional(readOnly = true)
    public Page<ContactResponseDto> getAllByUser(String email, String search, Pageable pageable) {

        User user = userRepository.findByEmailIgnoreCase(email).get();
        int userId = user.getId();

        if(StringUtils.hasText(search)){
            return contactRepository.searchContactsByUser(userId,search.toLowerCase(),search.toLowerCase(),pageable)
                    .map(contactMapper::entityToResponse);
        }
            return contactRepository.findByUser(user,pageable).map( contactMapper::entityToResponse);
    }

    @Transactional(rollbackFor = Exception.class)
    public ContactResponseDto create(String email, ContactRequestDto contactRequestDto) {

        Contact contact = contactMapper.requestToEntity(contactRequestDto);

        if (contactRequestDto.getType() != null) {
            ContactType contactType = contactTypeRepository.findByValueIgnoreCase(contactRequestDto.getType())
                    .orElseThrow(() -> new EntityNotFoundException("Invalid contact type."));

            contact.setContactType(contactType);
        }
        User user = userRepository.findByEmailIgnoreCase(email).get();
        contact.setUser(user);

        return contactMapper.entityToResponse(contactRepository.save(contact));
    }
    @Transactional(rollbackFor = Exception.class)
    public ErrorDetails uploadContactsFromCSV(String email, MultipartFile file)
            throws DataFormatException, ParseException, IOException {

        List<ContactRequestDto> requestDtos = csvMapper.CVSToContactRequestDto(file);

        User user = userRepository.findByEmailIgnoreCase(email).get();

        Set<String> contactTypes = contactTypeRepository.findAll().stream()
                .map(ContactType::getValue).collect(Collectors.toUnmodifiableSet());

        List<Long> invalidLines = new ArrayList<Long>();

        long line = 2;

        for(ContactRequestDto requestDto : requestDtos){
            if(!isRequestDtoValid(requestDto, validator, contactTypes, line)){
                invalidLines.add(line++);
                continue;
            }

            Contact contact = contactMapper.requestToEntity(requestDto);
            contact.setUser(user);
            contact.setContactType(contactTypeRepository.findByValueIgnoreCase(requestDto.getType()).orElse(null));

            contactRepository.save(contact);
            line++;
        }
        if(invalidLines.isEmpty()){
            return new ErrorDetails(LocalDateTime.now(),"Success","All entries uploaded.");
        }
        return new ErrorDetails(LocalDateTime.now(),"Uploaded "+ (requestDtos.size()-invalidLines.size()) +
                "/" + requestDtos.size() + " entries.",
                "Invalid entries on lines: " + invalidLines.toString());
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(String email, UUID contactGuid) throws EntityNotFoundException, AccessDeniedException {

        User user = userRepository.findByEmailIgnoreCase(email).get();
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

        User user = userRepository.findByEmailIgnoreCase(email).get();
        Contact contact = findByGuid(contactGuid);

        if (user.getId() == contact.getUser().getId()) {

            if (contactRequestDto.getType() != null) {
                ContactType contactType = contactTypeRepository.findByValueIgnoreCase(contactRequestDto.getType())
                        .orElseThrow(() -> new EntityNotFoundException("Invalid contact type."));
                contact.setContactType(contactType);
            }

            contactMapper.updateEntityFromRequest(contact, contactRequestDto);
            return contactMapper.entityToResponse(contactRepository.save(contact));
        }
        throw new AccessDeniedException("Contact does not belong to current user.");
    }

    @Transactional(readOnly = true)
    public ContactResponseDto getByGuid(String email, UUID contactGuid)
            throws EntityNotFoundException, AccessDeniedException {

        Contact contact = findByGuid(contactGuid);
        User user = userRepository.findByEmailIgnoreCase(email).get();

        if ((contact.getUser().getId() == user.getId()) || user.getRole().equals(UserRole.ADMIN)) {
            return contactMapper.entityToResponse(contact);
        }
        throw new AccessDeniedException("Contact does not belong to current user.");
    }

    private boolean isRequestDtoValid(ContactRequestDto contactRequestDto, Validator validator, Set<String> contactTypes, long line){

        List<String> violations =validator.validate(contactRequestDto).
                stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());

        String type = contactRequestDto.getType();

        boolean typeValid = contactTypes.stream().anyMatch(x -> x.equalsIgnoreCase(type));

        if(type != null && !typeValid) {
            violations.add("Invalid contact type.");
        }

        if(!violations.isEmpty()){
            System.out.println("Invalid entry on line "+ line + " " + violations);
            return false;
        }
        return true;
    }

    private Contact findByGuid(UUID guid) throws EntityNotFoundException {
        return contactRepository.findByGuid(guid)
                .orElseThrow(() -> new EntityNotFoundException("Contact not found."));
    }
}
