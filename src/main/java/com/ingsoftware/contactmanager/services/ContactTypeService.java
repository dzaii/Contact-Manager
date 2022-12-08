package com.ingsoftware.contactmanager.services;

import com.ingsoftware.contactmanager.dtos.ContactTypeRequestDto;
import com.ingsoftware.contactmanager.mappers.ContactTypeMapper;
import com.ingsoftware.contactmanager.models.ContactType;
import com.ingsoftware.contactmanager.repositories.ContactRepository;
import com.ingsoftware.contactmanager.repositories.ContactTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.directory.AttributeInUseException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ContactTypeService {

    private ContactTypeRepository contactTypeRepository;
    private ContactRepository contactRepository;
    private ContactTypeMapper contactTypeMapper;

    @Transactional(readOnly = true)
    public List<ContactType> getAll() {
        return contactTypeRepository.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    public ContactType create(ContactTypeRequestDto contactTypeRequestDto) {

        if (contactTypeRepository.existsContactTypeByValue(contactTypeRequestDto.getValue())) {
            throw new DuplicateKeyException("Contact type already exists.");
        }
        return contactTypeRepository.save(contactTypeMapper.requestToEntity(contactTypeRequestDto));
    }

    @Transactional(rollbackFor = Exception.class)
    public ContactType edit(ContactTypeRequestDto contactTypeRequestDto, UUID guid) throws EntityNotFoundException {

        if (contactTypeRepository.existsContactTypeByValue(contactTypeRequestDto.getValue())) {
            throw new DuplicateKeyException("Contact type already exists.");
        }

        ContactType contactType = findByGuid(guid);
        return contactTypeRepository.save(contactTypeMapper.updateEntityFromDto(contactType, contactTypeRequestDto));
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(UUID guid) throws EntityNotFoundException, AttributeInUseException {

        ContactType contactType = findByGuid(guid);

        if (contactRepository.findByContactType(contactType).isEmpty()) {
            contactTypeRepository.delete(contactType);
            return;
        }
        throw new AttributeInUseException("Contact type currently in use.");
    }

    private ContactType findByGuid(UUID guid) throws EntityNotFoundException {
        return contactTypeRepository.findByGuid(guid)
                .orElseThrow(() -> new EntityNotFoundException("Contact type not found."));
    }
}
