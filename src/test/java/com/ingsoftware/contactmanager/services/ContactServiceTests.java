package com.ingsoftware.contactmanager.services;

import com.ingsoftware.contactmanager.dtos.ContactRequestDto;
import com.ingsoftware.contactmanager.dtos.ContactResponseDto;
import com.ingsoftware.contactmanager.exeptionHandlers.ErrorDetails;
import com.ingsoftware.contactmanager.mappers.CSVMapper;
import com.ingsoftware.contactmanager.mappers.ContactMapper;
import com.ingsoftware.contactmanager.models.Contact;
import com.ingsoftware.contactmanager.models.ContactType;
import com.ingsoftware.contactmanager.models.User;
import com.ingsoftware.contactmanager.models.enums.UserRole;
import com.ingsoftware.contactmanager.repositories.ContactRepository;
import com.ingsoftware.contactmanager.repositories.ContactTypeRepository;
import com.ingsoftware.contactmanager.repositories.UserRepository;
import org.apache.el.parser.ParseException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.zip.DataFormatException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContactServiceTests {

    @Mock
    private ContactRepository contactRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ContactTypeRepository contactTypeRepository;
    @Mock
    private ContactMapper contactMapper;
    @Mock
    private CSVMapper csvMapper;
    @Mock
    private Validator validator;
    @InjectMocks
    private ContactService contactService;

    private User user;
    private User user2;
    private Contact contact;
    private ContactResponseDto contactResponseDto;
    private ContactRequestDto contactRequestDto;
    private ContactType contactType;

    @BeforeEach
    public void init(){

        user = new User();
        user.setId(1);
        user.setEmail("user@email.com");
        user.setPassword("password");

        user2 = new User();
        user2.setId(2);
        user2.setEmail("user2@email.com");
        user2.setPassword("password");

        contact = new Contact();
        contact.setFirstName("Test");
        contact.setLastName("Contact");

        contactResponseDto = new ContactResponseDto();
        contactResponseDto.setFirstName("Test");
        contactResponseDto.setLastName("Contact");

        contactRequestDto = new ContactRequestDto(
                "Test", "Contact",null,null,null,null,null);

        contactType = new ContactType();
        contactType.setValue("Type");
    }

    @Test
    public void ContactService_Create_ReturnsContactResponseDto(){

        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));
        when(contactMapper.requestToEntity(any(ContactRequestDto.class))).thenReturn(contact);
        when(contactMapper.entityToResponse(contact)).thenReturn(contactResponseDto);
        when(contactRepository.save(contact)).thenReturn(contact);

        ContactResponseDto expectedDto = contactService.create("email", contactRequestDto);

        Assertions.assertThat(expectedDto).isNotNull();
        Assertions.assertThat(contact.getUser()).isEqualTo(user);
        Assertions.assertThat(expectedDto.getFirstName()).isEqualTo(contact.getFirstName());


        verify(userRepository, times(1)).findByEmailIgnoreCase(anyString());
        verifyNoMoreInteractions(userRepository);

        verify(contactMapper, times(1)).requestToEntity(any(ContactRequestDto.class));
        verify(contactMapper, times(1)).entityToResponse(contact);
    }

    @Test
    public void ContactService_Create_SetsContactType(){

        contactRequestDto.setType("Type");

        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));
        when(contactMapper.requestToEntity(any(ContactRequestDto.class))).thenReturn(contact);
        when(contactMapper.entityToResponse(contact)).thenReturn(contactResponseDto);
        when(contactRepository.save(contact)).thenReturn(contact);
        when(contactTypeRepository.findByValueIgnoreCase(anyString())).thenReturn(Optional.of(contactType));

        ContactResponseDto expectedDto = contactService.create("email", contactRequestDto);

        Assertions.assertThat(expectedDto).isNotNull();
        Assertions.assertThat(contact.getContactType().getValue()).isEqualTo(contactRequestDto.getType());

        verify(contactTypeRepository, times(1)).findByValueIgnoreCase(anyString());
    }

    @Test
    public void ContactService_UploadContactsFromCSV_ReturnsWithSuccessMsg()
            throws DataFormatException, ParseException, IOException {



        when(csvMapper.CVSToContactRequestDto(any(MultipartFile.class))).
                thenReturn(Collections.singletonList(contactRequestDto));
        when(contactTypeRepository.findAll()).thenReturn(Collections.singletonList(contactType));

        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));
        when(validator.validate(contactRequestDto)).thenReturn(new HashSet<ConstraintViolation<ContactRequestDto>>());

        when(contactMapper.requestToEntity(any(ContactRequestDto.class))).thenReturn(contact);
        when(contactRepository.save(contact)).thenReturn(contact);
        when(contactTypeRepository.findByValueIgnoreCase(contactRequestDto.getType())).thenReturn(Optional.of(contactType));

        ErrorDetails expectedError = contactService
                .uploadContactsFromCSV("email", new MockMultipartFile("test",new byte[10]));

        Assertions.assertThat(expectedError.getMessage()).isEqualTo("Success");
    }

    @Test
    public void ContactService_UploadContactsFromCSV_ReturnsWithUploadedMsg()
            throws DataFormatException, ParseException, IOException {

        contactRequestDto.setType("invalid");

        when(csvMapper.CVSToContactRequestDto(any(MultipartFile.class))).
                thenReturn(Collections.singletonList(contactRequestDto));
        when(contactTypeRepository.findAll()).thenReturn(Collections.singletonList(contactType));

        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));
        when(validator.validate(contactRequestDto)).thenReturn(new HashSet<ConstraintViolation<ContactRequestDto>>());

        ErrorDetails expectedError = contactService
                .uploadContactsFromCSV("email", new MockMultipartFile("test",new byte[10]));

        Assertions.assertThat(expectedError.getMessage()).contains("Uploaded ");
    }

    @Test
    public void ContactService_Create_DoesNotFindContactType(){

        contactRequestDto.setType("Type");


        when(contactMapper.requestToEntity(any(ContactRequestDto.class))).thenReturn(contact);
        when(contactTypeRepository.findByValueIgnoreCase(anyString())).thenReturn(Optional.empty());

        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(
                EntityNotFoundException.class, () -> contactService.create("email", contactRequestDto));

        String expectedMessage = "Invalid contact type.";
        String actualMessage = exception.getMessage();
        Assertions.assertThat(actualMessage.contains(expectedMessage)).isTrue();
    }

    @Test
    public void ContactService_Edit_ReturnsContactResponseDto() throws AccessDeniedException {

        contact.setUser(user);

        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));
        when(contactRepository.findByGuid(any(UUID.class))).thenReturn(Optional.of(contact));
        when(contactMapper.updateEntityFromRequest(contact,contactRequestDto)).thenReturn(contact);
        when(contactRepository.save(contact)).thenReturn(contact);
        when(contactMapper.entityToResponse(contact)).thenReturn(contactResponseDto);

        ContactResponseDto expectedDto = contactService.edit("email",UUID.randomUUID(),contactRequestDto);

        Assertions.assertThat(expectedDto).isNotNull();
        Assertions.assertThat(expectedDto.getFirstName()).isEqualTo(contactRequestDto.getFirstName());
        verifyNoInteractions(contactTypeRepository);
    }

    @Test
    public void ContactService_Edit_ContactDoesNotBelongToUser(){

        contact.setUser(user2);

        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));
        when(contactRepository.findByGuid(any(UUID.class))).thenReturn(Optional.of(contact));


        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(
                AccessDeniedException.class, () -> contactService.edit("email", UUID.randomUUID(),contactRequestDto));

        String expectedMessage = "Contact does not belong to current user.";
        String actualMessage = exception.getMessage();

        Assertions.assertThat(actualMessage.contains(expectedMessage)).isTrue();
        verifyNoInteractions(contactTypeRepository);

    }

    @Test
    public void ContactService_Edit_DoesNotFindContactType() {

        contact.setUser(user);
        contactRequestDto.setType(contactType.getValue());

        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));
        when(contactRepository.findByGuid(any(UUID.class))).thenReturn(Optional.of(contact));
        when(contactTypeRepository.findByValueIgnoreCase(contactRequestDto.getType())).thenReturn(Optional.empty());


        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(
                EntityNotFoundException.class, () -> contactService.edit("email",UUID.randomUUID(), contactRequestDto));

        String expectedMessage = "Invalid contact type.";
        String actualMessage = exception.getMessage();

        Assertions.assertThat(actualMessage.contains(expectedMessage)).isTrue();
    }

    @Test
    public void ContactService_GetByGuid_ReturnsContactResponseDtoToOwner() throws AccessDeniedException {

        contact.setUser(user);

        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));
        when(contactRepository.findByGuid(any(UUID.class))).thenReturn(Optional.of(contact));
        when(contactMapper.entityToResponse(contact)).thenReturn(contactResponseDto);

        ContactResponseDto expectedDto = contactService.getByGuid("email",UUID.randomUUID());

        Assertions.assertThat(expectedDto).isNotNull();
        Assertions.assertThat(expectedDto.getFirstName()).isEqualTo(contactRequestDto.getFirstName());
    }

    @Test
    public void ContactService_GetByGuid_ReturnsContactResponseDtoToAdmin() throws AccessDeniedException {

        user.setRole(UserRole.ADMIN);
        contact.setUser(user2);

        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));
        when(contactRepository.findByGuid(any(UUID.class))).thenReturn(Optional.of(contact));
        when(contactMapper.entityToResponse(contact)).thenReturn(contactResponseDto);

        ContactResponseDto expectedDto = contactService.getByGuid("email",UUID.randomUUID());

        Assertions.assertThat(expectedDto).isNotNull();
        Assertions.assertThat(expectedDto.getFirstName()).isEqualTo(contactRequestDto.getFirstName());
    }

    @Test
    public void ContactService_GetByGuid_UserDoesNotHaveAuthority() {

        user.setRole(UserRole.USER);
        contact.setUser(user2);

        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));
        when(contactRepository.findByGuid(any(UUID.class))).thenReturn(Optional.of(contact));


        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(
                AccessDeniedException.class, () -> contactService.getByGuid("email", UUID.randomUUID()));

        String expectedMessage = "Contact does not belong to current user.";
        String actualMessage = exception.getMessage();

        Assertions.assertThat(actualMessage.contains(expectedMessage)).isTrue();
    }

    @Test
    public void ContactService_Delete_ReturnsVoid(){

        contact.setUser(user);

        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));
        when(contactRepository.findByGuid(any(UUID.class))).thenReturn(Optional.of(contact));

        assertAll(() -> contactService.delete("email",UUID.randomUUID()));
    }

    @Test
    public void ContactService_Delete_UserDoesNotHaveAuthority() {

        contact.setUser(user2);


        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));
        when(contactRepository.findByGuid(any(UUID.class))).thenReturn(Optional.of(contact));

        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(
                AccessDeniedException.class, () -> contactService.delete("email", UUID.randomUUID()));

        String expectedMessage = "Contact does not belong to current user.";
        String actualMessage = exception.getMessage();

        Assertions.assertThat(actualMessage.contains(expectedMessage)).isTrue();
    }

    @Test
    public void ContactService_GetAll_ReturnsUnfilteredPage() {

        when(contactRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<Contact>(Arrays.asList(contact)));
        when(contactMapper.entityToResponse(contact)).thenReturn(contactResponseDto);

        Page<ContactResponseDto> expectedPage = contactService.getAll("", PageRequest.of(0,10));

        Assertions.assertThat(expectedPage.getContent().contains(contactResponseDto)).isTrue();
        verify(contactRepository, times(1)).findAll(PageRequest.of(0,10));
        verifyNoMoreInteractions(contactRepository);
    }

    @Test
    public void ContactService_GetAll_ReturnsFilteredPage() {

        when(contactRepository.searchAllContacts(any(String.class),any(String.class),any(Pageable.class)))
                .thenReturn(new PageImpl<Contact>(Arrays.asList(contact)));

        when(contactMapper.entityToResponse(contact)).thenReturn(contactResponseDto);

        Page<ContactResponseDto> expectedPage = contactService.getAll("test", PageRequest.of(0,10));

        Assertions.assertThat(expectedPage.getContent().contains(contactResponseDto)).isTrue();

        verify(contactRepository, times(1))
                .searchAllContacts("test","test",PageRequest.of(0,10));

        verifyNoMoreInteractions(contactRepository);
    }

    @Test
    public void ContactService_GetAllByUser_ReturnsFilteredPage() {

        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));
        when(contactRepository.searchContactsByUser(anyInt(),any(String.class),any(String.class),any(Pageable.class)))
                .thenReturn(new PageImpl<Contact>(Arrays.asList(contact)));
        when(contactMapper.entityToResponse(contact)).thenReturn(contactResponseDto);

        Page<ContactResponseDto> expectedPage = contactService.getAllByUser(
                "email","test", PageRequest.of(0,10));

        Assertions.assertThat(expectedPage.getContent().contains(contactResponseDto)).isTrue();

        verify(contactRepository, times(1))
                .searchContactsByUser(user.getId(),"test","test",PageRequest.of(0,10));

        verifyNoMoreInteractions(contactRepository);
    }

    @Test
    public void ContactService_GetAllByUser_ReturnsUnfilteredPage() {

        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.of(user));
        when(contactRepository.findByUser(user, PageRequest.of(0,10)))
                .thenReturn(new PageImpl<Contact>(Arrays.asList(contact)));
        when(contactMapper.entityToResponse(contact)).thenReturn(contactResponseDto);

        Page<ContactResponseDto> expectedPage = contactService.getAllByUser(
                "email",null, PageRequest.of(0,10));

        Assertions.assertThat(expectedPage.getContent().contains(contactResponseDto)).isTrue();

        verify(contactRepository, times(1))
                .findByUser(user,PageRequest.of(0,10));

        verifyNoMoreInteractions(contactRepository);
    }

    
}
