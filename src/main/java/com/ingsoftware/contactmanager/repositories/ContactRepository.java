package com.ingsoftware.contactmanager.repositories;

import com.ingsoftware.contactmanager.models.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact,Integer> {
}
