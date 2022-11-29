package com.ingsoftware.contactmanager.repositories;

import com.ingsoftware.contactmanager.models.Contact;
import com.ingsoftware.contactmanager.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContactRepository extends JpaRepository<Contact,Integer> {

    List<Contact> findByUser(User user);
    Optional<Contact> findByGuid(UUID guid);

    @Transactional
    @Modifying
    @Query(value = "UPDATE contacts SET type_id = NULL WHERE type_id = :id", nativeQuery = true)
    void setTypeIdToNull(@Param("id") int x);
}
