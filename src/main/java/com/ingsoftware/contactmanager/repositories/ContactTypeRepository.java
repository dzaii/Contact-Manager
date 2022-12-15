package com.ingsoftware.contactmanager.repositories;

import com.ingsoftware.contactmanager.models.ContactType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContactTypeRepository extends JpaRepository<ContactType,Integer> {

    boolean existsContactTypeByValueIgnoreCase(String value);
    Optional<ContactType> findByGuid(UUID guid);
    Optional<ContactType> findByValueIgnoreCase(String value);
}
