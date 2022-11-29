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

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class ContactTypeService {

    private ContactTypeRepository contactTypeRepository;
    private ContactRepository contactRepository;
    private ContactTypeMapper contactTypeMapper;

    public List<ContactType> getAll(){
        return contactTypeRepository.findAll();
    }

    public ContactType createOrUpdate(ContactTypeRequestDto contactTypeRequestDto){
        if(contactTypeRepository.existsContactTypeByValue(contactTypeRequestDto.getValue()))
            throw new DuplicateKeyException("Contact type already exists.");

        Optional<ContactType> contactTypeOptional = contactTypeRepository.findById(contactTypeRequestDto.getTypeId());
        if(contactTypeOptional.isPresent()){
            ContactType contactTypeOld = contactTypeOptional.get();
            contactTypeOld.setValue(contactTypeRequestDto.getValue());
            return contactTypeRepository.save(contactTypeOld);
        }
        return contactTypeRepository.save(contactTypeMapper.requestToEntity(contactTypeRequestDto));
    }

    public String delete(int id) throws InstanceNotFoundException{
        Optional<ContactType> contactTypeOptional = contactTypeRepository.findById(id);

        if(contactTypeOptional.isPresent()){

            contactRepository.setTypeIdToNull(id);
            contactTypeRepository.deleteById(id);

            return "Deleted contact type.";
        }
        throw new InstanceNotFoundException("Contact type not found.");
    }
}
