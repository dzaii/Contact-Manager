package com.ingsoftware.contactmanager;

import com.ingsoftware.contactmanager.models.Contact;
import com.ingsoftware.contactmanager.models.User;
import com.ingsoftware.contactmanager.models.enums.UserRole;
import com.ingsoftware.contactmanager.services.ContactService;
import com.ingsoftware.contactmanager.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.time.LocalDateTime;

@SpringBootApplication
public class ContactManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContactManagerApplication.class, args);
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf().disable();
		return http.build();
	}
}
