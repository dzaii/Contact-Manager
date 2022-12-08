package com.ingsoftware.contactmanager.services;

import com.ingsoftware.contactmanager.dtos.UserRequestDto;
import com.ingsoftware.contactmanager.dtos.UserResponseDto;
import com.ingsoftware.contactmanager.mappers.UserMapper;
import com.ingsoftware.contactmanager.models.User;
import com.ingsoftware.contactmanager.repositories.UserRepository;
import lombok.AllArgsConstructor;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private UserMapper userMapper;

    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::entityToResponseDto);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getByGuid(UUID guid) throws EntityNotFoundException {
        return userMapper.entityToResponseDto(findByGuid(guid));
    }

    @Transactional(rollbackFor = Exception.class)
    public UserResponseDto create(UserRequestDto userRequestDto) {

        if (userRepository.existsUserByEmail(userRequestDto.getEmail())) {
            throw new DuplicateKeyException("Email already exists.");
        }

        User user = userMapper.requestDtoToEntity(userRequestDto);
        return userMapper.entityToResponseDto(userRepository.save(user));
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(UUID guid) throws EntityNotFoundException {

        if (userRepository.deleteByGuid(guid) == 0) {
            throw new EntityNotFoundException("User not found.");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public UserResponseDto edit(UserRequestDto userRequestDto, UUID guid)
            throws EntityNotFoundException, DuplicateKeyException {

        User user = findByGuid(guid);

        if (user.getEmail().equals(userRequestDto.getEmail())
                || !userRepository.existsUserByEmail(userRequestDto.getEmail())) {

            return userMapper.entityToResponseDto(userRepository.save(
                    userMapper.updateEntityFromRequest(user, userRequestDto)));
        }
        throw new DuplicateKeyException("Email already exist.");
    }


    private User findByGuid(UUID guid) throws EntityNotFoundException {
        return userRepository.findByGuid(guid)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));
    }
}
