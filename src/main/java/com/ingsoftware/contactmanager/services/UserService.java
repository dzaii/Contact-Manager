package com.ingsoftware.contactmanager.services;

import com.ingsoftware.contactmanager.dtos.UserRequestWithRoleDto;
import com.ingsoftware.contactmanager.dtos.UserResponseDto;
import com.ingsoftware.contactmanager.mappers.UserMapper;
import com.ingsoftware.contactmanager.models.User;
import com.ingsoftware.contactmanager.models.enums.UserRole;
import com.ingsoftware.contactmanager.repositories.UserRepository;
import lombok.AllArgsConstructor;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.nio.file.AccessDeniedException;
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
    public UserResponseDto get(String email, UUID guid) throws EntityNotFoundException, AccessDeniedException {

        User currentUser = userRepository.findByEmailIgnoreCase(email).get();

        if(currentUser.getGuid().equals(guid) || currentUser.getRole().equals(UserRole.ADMIN)) {
            return userMapper.entityToResponseDto(findByGuid(guid));
        }
        throw new AccessDeniedException("Not authorized.");
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
    public void delete(String email, UUID guid) throws EntityNotFoundException, AccessDeniedException {

        User currentUser = userRepository.findByEmailIgnoreCase(email).get();

        if(currentUser.getGuid().equals(guid) || currentUser.getRole().equals(UserRole.ADMIN)) {
               User targetUser = findByGuid(guid);
               userRepository.delete(targetUser);
            }
        throw new AccessDeniedException("Not authorized.");
    }

    @Transactional(rollbackFor = Exception.class)
    public UserResponseDto edit(String email, UserRequestWithRoleDto userRequestWithRoleDto, UUID guid)
            throws EntityNotFoundException, DuplicateKeyException, AccessDeniedException {

        User currentUser = userRepository.findByEmailIgnoreCase(email).get();
        User targetUser = findByGuid(guid);

        validateEditRequest(currentUser,targetUser,userRequestWithRoleDto);

        if (userRequestWithRoleDto.getPhoneNumber().equals(targetUser.getPhoneNumber())
                || currentUser.getRole().equals(UserRole.ADMIN)){

            userMapper.updateEntityFromRequest(targetUser, userRequestWithRoleDto);
            userRepository.save(targetUser);

            return userMapper.entityToResponseDto(targetUser);
        }

        userMapper.updateEntityFromRegister(targetUser, userRequestWithRoleDto);
        registrationService.sendCode(targetUser);
        targetUser.setEnabled(false);
        userRepository.save(targetUser);

        return userMapper.entityToResponseDto(targetUser);
    }


    private void validateEditRequest(User currentUser, User targetUser, UserRequestWithRoleDto userRequestWithRoleDto )
            throws AccessDeniedException {

        if(!currentUser.getGuid().equals(targetUser.getGuid()) && !currentUser.getRole().equals(UserRole.ADMIN)){
            throw new AccessDeniedException("Not authorized to change other users.");
        }

        if (!targetUser.getEmail().equals(userRequestWithRoleDto.getEmail())
                && userRepository.existsUserByEmailIgnoreCase(userRequestWithRoleDto.getEmail())) {
            throw new DuplicateKeyException("Email already exist.");
        }

        if (!targetUser.getPhoneNumber().equals(userRequestWithRoleDto.getPhoneNumber()) &&
                userRepository.existsUserByPhoneNumber(userRequestWithRoleDto.getPhoneNumber())) {
            throw new DuplicateKeyException("Phone number already exists.");
        }

        if(!userRequestWithRoleDto.getRole().equalsIgnoreCase(UserRole.USER.name())
                && !currentUser.getRole().equals(UserRole.ADMIN)){
            throw new AccessDeniedException("Not authorized to change user roles.");
        }
    }

    private User findByGuid(UUID guid) throws EntityNotFoundException {
        return userRepository.findByGuid(guid)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));
    }

}
