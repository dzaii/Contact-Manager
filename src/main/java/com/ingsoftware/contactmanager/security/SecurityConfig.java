package com.ingsoftware.contactmanager.security;


import com.ingsoftware.contactmanager.models.enums.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/users/register").permitAll()
                .antMatchers("/users/contacts/**").hasAnyAuthority()
                .antMatchers("/users/**").hasAuthority(UserRole.ADMIN.name())
                .antMatchers("/contacts").hasAuthority(UserRole.ADMIN.name())
                .antMatchers("/contacts/types/**").hasAuthority(UserRole.ADMIN.name())
                .anyRequest().authenticated()
                .and()
                .httpBasic();


        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
