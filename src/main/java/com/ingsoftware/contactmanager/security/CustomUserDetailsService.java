package com.ingsoftware.contactmanager.security;

import com.ingsoftware.contactmanager.models.User;
import com.ingsoftware.contactmanager.models.enums.UserRole;
import com.ingsoftware.contactmanager.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
@Data
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user =
                userRepository.findByEmailIgnoreCase(email).orElseThrow(() -> new UsernameNotFoundException("Email not found"));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                mapRolesToAuthorities(user.getRole()));
    }

    private List<GrantedAuthority> mapRolesToAuthorities(UserRole role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }
}
