package com.ingsoftware.contactmanager.services;

import com.ingsoftware.contactmanager.dtos.UserRequestDto;
import com.ingsoftware.contactmanager.dtos.UserResponseDto;
import com.ingsoftware.contactmanager.mappers.UserMapper;
import com.ingsoftware.contactmanager.models.User;
import com.ingsoftware.contactmanager.repositories.UserRepository;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private UserMapper userMapper;

    public List<UserResponseDto> getAll(){
        return userMapper.entityToResponseDto(userRepository.findAll());
    }

    public UserResponseDto getByGuid(UUID guid) throws InstanceNotFoundException {
        Optional<User> user = userRepository.findByGuid(guid);
        if(user.isPresent())
            return userMapper.entityToResponseDto(user.get());
        else
            throw new InstanceNotFoundException("Invalid guid.");
    }

    public UserResponseDto registerUser(UserRequestDto userRequestDto){
       return userMapper.entityToResponseDto((userRepository.save(userMapper.requestDtoToEntity(userRequestDto))));
    }

    public void deleteUser(UUID guid) throws InstanceNotFoundException {

        if(userRepository.deleteByGuid(guid)==0)
            throw new InstanceNotFoundException("Invalid guid.");
    }

    public UserResponseDto editUser(UserRequestDto userRequestDto, UUID guid) throws InstanceNotFoundException {

        Optional<User> userOpt = userRepository.findByGuid(guid);

        if(userOpt.isPresent()){

           User user = userOpt.get();

           user.setEmail(userRequestDto.getEmail());
           user.setFirstName(userRequestDto.getFirstName());
           user.setLastName(userRequestDto.getLastName());
           user.setPassword(userRequestDto.getPassword());

           return userMapper.entityToResponseDto(userRepository.save(user));
        }
        else
            throw new InstanceNotFoundException("Invalid guid.");
    }
}
