package com.ingsoftware.contactmanager.services;

import com.ingsoftware.contactmanager.dtos.UserRequestDto;
import com.ingsoftware.contactmanager.dtos.UserResponseDto;
import com.ingsoftware.contactmanager.mappers.UserMapper;
import com.ingsoftware.contactmanager.models.User;
import com.ingsoftware.contactmanager.models.enums.UserRole;
import com.ingsoftware.contactmanager.repositories.UserRepository;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;



@AllArgsConstructor
@Service
public class RegistrationService {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private EmailService emailService;



    public Map<UserResponseDto,String> register(UserRequestDto userRequestDto) {

        if (userRepository.existsUserByEmailIgnoreCase(userRequestDto.getEmail())) {
            throw new DuplicateKeyException("Email already exists.");
        }
        if (userRepository.existsUserByPhoneNumber(userRequestDto.getPhoneNumber())) {
            throw new DuplicateKeyException("Phone number already exists.");
        }

        User user = userMapper.registerDtoToEntity(userRequestDto);
        sendCode(user);
        user.setRole(UserRole.USER);

        Map<UserResponseDto,String> response = new HashMap<>();
        response.put(userMapper.entityToResponseDto(userRepository.save(user)), "Submit code on: /register/" + user.getGuid());
        return response;

    }

    public void verify(String code, UUID guid){
        User user = userRepository.findByGuid(guid).orElseThrow(() -> new EntityNotFoundException("User not found."));

        Twilio.init(System.getenv("TWILIO_ACCOUNT_SID"), System.getenv("TWILIO_AUTH_TOKEN"));
        VerificationCheck verificationCheck = VerificationCheck.creator(
                        System.getenv("TWILIO_SERVICE_SID"))
                .setTo(user.getPhoneNumber())
                .setCode(code)
                .create();
        if(!verificationCheck.getStatus().equals("approved")){
            throw new ValidationException("Incorrect code.");
        }
        user.setEnabled(true);
        userRepository.save(user);
        emailService.sendConfirmationEmail(user.getEmail());
    }

    public void sendCode(User user){
        Twilio.init(System.getenv("TWILIO_ACCOUNT_SID"), System.getenv("TWILIO_AUTH_TOKEN"));
        Verification verification = Verification.creator(
                        System.getenv("TWILIO_SERVICE_SID"),
                        user.getPhoneNumber(),
                        "sms")
                .create();
    }
}
