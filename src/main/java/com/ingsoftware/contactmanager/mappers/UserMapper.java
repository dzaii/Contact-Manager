package com.ingsoftware.contactmanager.mappers;

import com.ingsoftware.contactmanager.dtos.UserRequestDto;
import com.ingsoftware.contactmanager.dtos.UserRequestWithRoleDto;
import com.ingsoftware.contactmanager.dtos.UserResponseDto;
import com.ingsoftware.contactmanager.models.User;
import com.ingsoftware.contactmanager.models.enums.UserRole;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;


@Mapper
public abstract class UserMapper {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "contactList", ignore = true)
    @Mapping(target = "guid", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    public abstract User requestDtoToEntity(UserRequestWithRoleDto userRequestWithRoleDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "contactList", ignore = true)
    @Mapping(target = "guid", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    public abstract User registerDtoToEntity(UserRequestDto userRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "contactList", ignore = true)
    @Mapping(target = "guid", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    public abstract User updateEntityFromRequest(@MappingTarget User user, UserRequestWithRoleDto userRequestWithRoleDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "contactList", ignore = true)
    @Mapping(target = "guid", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    public abstract User updateEntityFromRegister(@MappingTarget User user, UserRequestDto userRequestDto);

    @AfterMapping
    protected void AddRoleSetEnabledEncodePassword(@MappingTarget User user,
                                                   UserRequestWithRoleDto userRequestWithRoleDto){

        user.setRole(UserRole.valueOf(userRequestWithRoleDto.getRole().toUpperCase()));
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(userRequestWithRoleDto.getPassword()));
    }

    @AfterMapping
    protected void encodePassword(@MappingTarget User user, UserRequestDto userRequestDto){
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
    }

    public abstract UserResponseDto entityToResponseDto(User user);

}
