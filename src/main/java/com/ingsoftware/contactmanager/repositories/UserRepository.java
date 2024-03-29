package com.ingsoftware.contactmanager.repositories;

import com.ingsoftware.contactmanager.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsUserByEmailIgnoreCase(String email);
    boolean existsUserByPhoneNumber(String phoneNumber);

    Long deleteByGuid(UUID guid);

    Optional<User> findByGuid(UUID guid);
    Optional<User> findByEmailIgnoreCase(String email);
}
