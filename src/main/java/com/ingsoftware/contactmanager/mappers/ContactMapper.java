package com.ingsoftware.contactmanager.mappers;

import com.ingsoftware.contactmanager.dtos.ContactRequestDto;
import com.ingsoftware.contactmanager.dtos.ContactResponseDto;
import com.ingsoftware.contactmanager.models.Contact;
import org.mapstruct.*;

import java.util.List;

@Mapper
public abstract class ContactMapper {

    @Mapping(target = "type", ignore = true)
    public abstract ContactResponseDto entityToResponse(Contact contact);

    @Mapping(target = "type", ignore = true)
    public abstract List<ContactResponseDto> entityToResponse(List<Contact> contacts);

    @AfterMapping
    protected void showContactType(Contact contact, @MappingTarget ContactResponseDto contactResponseDto){
        if(contact.getContactType()!= null)
            contactResponseDto.setType(contact.getContactType().getValue());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "contactType", ignore = true)
    @Mapping(target = "guid", ignore = true)
    @Mapping(target = "user", ignore = true)
    public abstract Contact requestToEntity(ContactRequestDto contactRequestDto);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "contactType", ignore = true)
    @Mapping(target = "guid", ignore = true)
    @Mapping(target = "user", ignore = true)
    public abstract Contact updateEntityFromRequest(@MappingTarget Contact contact, ContactRequestDto contactRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "contactType", ignore = true)
    @Mapping(target = "guid", ignore = true)
    @Mapping(target = "user", ignore = true)
    public abstract List<Contact> requestToEntity(List<ContactRequestDto> contactRequestDtos);


}
