package com.ingsoftware.contactmanager;

import com.ingsoftware.contactmanager.models.Contact;
import com.ingsoftware.contactmanager.models.User;
import com.ingsoftware.contactmanager.models.enums.ContactType;
import com.ingsoftware.contactmanager.models.enums.UserRole;
import com.ingsoftware.contactmanager.services.ContactService;
import com.ingsoftware.contactmanager.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class ContactManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContactManagerApplication.class, args);
	}
//	@Bean
//	CommandLineRunner run(UserService userService, ContactService contactService){
//		return args -> {
//			User user1 = new User(0, "Ivan", "Bojnovic", "dsdsa@gmail.com",
//					              "pass", UserRole.ADMIN, LocalDateTime.now(),LocalDateTime.now(),null);
//			userService.save(user1);
//
//			User user2 = new User(0, "Ivan", "Bojnovic", "dsdsssa@gmail.com",
//					"pass", UserRole.ADMIN, LocalDateTime.now(),LocalDateTime.now(),null);
//			userService.save(user2);
//
//			Contact contact = new Contact(0, "Ivan", "Bojnovic", "dsdsssa@gmail.com",
//					"0654587895", LocalDateTime.now(),LocalDateTime.now(), ContactType.FAMILY,user2);
//			contactService.save(contact);
//
//
//		};
//
//	};
}
