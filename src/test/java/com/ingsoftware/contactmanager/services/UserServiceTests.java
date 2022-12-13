package com.ingsoftware.contactmanager.services;

import com.ingsoftware.contactmanager.dtos.UserResponseDto;
import com.ingsoftware.contactmanager.mappers.UserMapper;
import com.ingsoftware.contactmanager.models.User;
import com.ingsoftware.contactmanager.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;


    @Test
    public void userService_GetByGuid_ReturnsOneUserResponseDto(){

        final User testUser = new User();
                   testUser.setId(1);
                   testUser.setEmail("user@email.com");
                   testUser.setPassword("password");

        final UserResponseDto testDto = new UserResponseDto();
        testDto.setEmail("user@email.com");

        when(userRepository.findByGuid(any(UUID.class))).thenReturn(Optional.of(testUser));
        when(userMapper.entityToResponseDto(testUser)).thenReturn(testDto);

        UserResponseDto expectedDto = userService.getByGuid(UUID.randomUUID());

        Assertions.assertThat(expectedDto).isNotNull();
        Assertions.assertThat(expectedDto.getEmail()).isEqualTo(testUser.getEmail());

        verify(userRepository, times(1)).findByGuid(any(UUID.class));
        verifyNoMoreInteractions(userRepository);

        verify(userMapper, times(1)).entityToResponseDto(testUser);
        verifyNoMoreInteractions(userMapper);
    }
}
