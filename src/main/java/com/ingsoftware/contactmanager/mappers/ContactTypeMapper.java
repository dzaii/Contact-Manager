package com.ingsoftware.contactmanager.mappers;


import com.ingsoftware.contactmanager.dtos.ContactTypeRequestDto;
import com.ingsoftware.contactmanager.models.ContactType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public abstract class ContactTypeMapper {


    @Mapping(target = "typeId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "guid", ignore = true)
    public abstract ContactType requestToEntity(ContactTypeRequestDto contactTypeRequestDto);

    @Mapping(target = "typeId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "guid", ignore = true)
    public abstract ContactType updateEntityFromDto(@MappingTarget ContactType contactType, ContactTypeRequestDto contactTypeRequestDto);
}
