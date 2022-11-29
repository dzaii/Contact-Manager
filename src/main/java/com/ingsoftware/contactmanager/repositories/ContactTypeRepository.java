package com.ingsoftware.contactmanager.repositories;

import com.ingsoftware.contactmanager.models.ContactType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactTypeRepository extends JpaRepository<ContactType,Integer> {

    boolean existsContactTypeByValue(String value);
}
