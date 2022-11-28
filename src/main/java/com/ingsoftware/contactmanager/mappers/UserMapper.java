package com.ingsoftware.contactmanager.mappers;

import com.ingsoftware.contactmanager.dtos.UserRequestDto;
import com.ingsoftware.contactmanager.dtos.UserResponseDto;
import com.ingsoftware.contactmanager.models.User;
import com.ingsoftware.contactmanager.models.enums.UserRole;
import org.mapstruct.*;

import java.util.List;


@Mapper
public abstract class UserMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "modifiedAt", ignore = true)
    @Mapping(target = "contactList", ignore = true)
    @Mapping(target = "guid", ignore = true)
    public abstract User requestDtoToEntity(UserRequestDto userRequestDto);

    @AfterMapping
    protected void addRoleToUser(@MappingTarget User user){
        user.setRole(UserRole.USER);
    }

    public abstract UserResponseDto entityToResponseDto(User user);

    public abstract List<UserResponseDto> entityToResponseDto(List<User> users);
}
