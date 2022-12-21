package com.ingsoftware.contactmanager.services;

import com.ingsoftware.contactmanager.dtos.UserRequestDto;
import com.ingsoftware.contactmanager.dtos.UserRequestWithRoleDto;
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
    private EmailService emailService;
    private RegistrationService registrationService;

    @Transactional(readOnly = true)
    public Page<UserResponseDto> getAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::entityToResponseDto);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getByGuid(UUID guid) throws EntityNotFoundException {
        return userMapper.entityToResponseDto(findByGuid(guid));
    }

    @Transactional(rollbackFor = Exception.class)
    public UserResponseDto create(UserRequestWithRoleDto userRequestWithRoleDto) {

        if (userRepository.existsUserByEmailIgnoreCase(userRequestWithRoleDto.getEmail())) {
            throw new DuplicateKeyException("Email already exists.");
        }
        if (userRepository.existsUserByPhoneNumber(userRequestWithRoleDto.getPhoneNumber())) {
            throw new DuplicateKeyException("Phone number already exists.");
        }

        User user = userMapper.requestDtoToEntity(userRequestWithRoleDto);
        emailService.sendConfirmationEmail(user.getEmail());

        return userMapper.entityToResponseDto(userRepository.save(user));
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(UUID guid) throws EntityNotFoundException {

        if (userRepository.deleteByGuid(guid) == 0) {
            throw new EntityNotFoundException("User not found.");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public UserResponseDto edit(UserRequestWithRoleDto userRequestWithRoleDto, UUID guid)
            throws EntityNotFoundException, DuplicateKeyException {

        User user = findByGuid(guid);

        if (user.getEmail().equals(userRequestWithRoleDto.getEmail())
                || !userRepository.existsUserByEmailIgnoreCase(userRequestWithRoleDto.getEmail())) {

            if(user.getPhoneNumber().equals(userRequestWithRoleDto.getPhoneNumber())
                    || !userRepository.existsUserByPhoneNumber(userRequestWithRoleDto.getPhoneNumber())) {

                return userMapper.entityToResponseDto(userRepository.save(
                        userMapper.updateEntityFromRequest(user, userRequestWithRoleDto)));
            }
            throw new DuplicateKeyException("Phone number already exists.");
        }
        throw new DuplicateKeyException("Email already exist.");
    }


    private User findByGuid(UUID guid) throws EntityNotFoundException {
        return userRepository.findByGuid(guid)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));
    }

    @Transactional(rollbackFor = Exception.class)
    public UserResponseDto editCurrent(UserRequestDto userRequestDto, String email)
            throws DuplicateKeyException {

        User user = userRepository.findByEmailIgnoreCase(email).get();

        if (user.getEmail().equals(userRequestDto.getEmail())
                || !userRepository.existsUserByEmailIgnoreCase(userRequestDto.getEmail())) {

            if(user.getPhoneNumber().equals(userRequestDto.getPhoneNumber())) {

                return userMapper.entityToResponseDto(userRepository.save(
                        userMapper.updateEntityFromRegister(user, userRequestDto)));
            }
            if (userRepository.existsUserByPhoneNumber(userRequestDto.getPhoneNumber())) {
                throw new DuplicateKeyException("Phone number already exists.");
            }

            userMapper.updateEntityFromRegister(user, userRequestDto);
            registrationService.sendCode(user);
            user.setEnabled(false);

            return userMapper.entityToResponseDto(userRepository.save(user));

        }
        throw new DuplicateKeyException("Email already exist.");
    }

    @Transactional(readOnly = true)
    public UserResponseDto getCurrent(String email) {
        return userMapper.entityToResponseDto(userRepository.findByEmailIgnoreCase(email).get());
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteCurrent(String email) {
        userRepository.delete(userRepository.findByEmailIgnoreCase(email).get());
    }
}
