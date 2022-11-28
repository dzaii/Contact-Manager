package com.ingsoftware.contactmanager.repositories;

import com.ingsoftware.contactmanager.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    Long deleteByGuid(UUID guid);
    Optional<User> findByGuid(UUID guid);
}
