package com.ingsoftware.contactmanager.mappers;

import com.ingsoftware.contactmanager.dtos.UserRequestDto;
import com.ingsoftware.contactmanager.dtos.UserResponseDto;
import com.ingsoftware.contactmanager.models.User;
import com.ingsoftware.contactmanager.models.enums.UserRole;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;


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
    public abstract User requestDtoToEntity(UserRequestDto userRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "contactList", ignore = true)
    @Mapping(target = "guid", ignore = true)
    public abstract User updateEntityFromRequest(@MappingTarget User user, UserRequestDto userRequestDto);

    @AfterMapping
    protected void roleAndPass(@MappingTarget User user, UserRequestDto userRequestDto){
        user.setRole(UserRole.valueOf(userRequestDto.getRole().toUpperCase()));
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
    }

    public abstract UserResponseDto entityToResponseDto(User user);

    public abstract List<UserResponseDto> entityToResponseDto(List<User> users);
}
