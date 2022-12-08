package com.ingsoftware.contactmanager.repositories;

import com.ingsoftware.contactmanager.models.Contact;
import com.ingsoftware.contactmanager.models.ContactType;
import com.ingsoftware.contactmanager.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContactRepository extends JpaRepository<Contact,Integer>, JpaSpecificationExecutor<Contact> {

    Page<Contact> findByUser(User user, Pageable pageable);
    Optional<Contact> findByGuid(UUID guid);
    List<Contact> findByContactType(ContactType contactType);


    @Query(value = "SELECT * FROM contacts WHERE  " +

            "       (LOWER(first_name)   like :search% " +
            "     or LOWER(last_name)    like :search% " +
            "     or LOWER(email)        like %:search2% " +
            "     or LOWER(phone_number) like %:search2%)" +

            "     order by  (LOWER(first_name) like :search% or LOWER(last_name) like :search%) desc nulls last," +
            "                COALESCE(first_name, last_name, email, phone_number)",
            nativeQuery = true)
    Page<Contact> searchAllContacts(@Param("search") String search,
                                    @Param("search2") String search2, Pageable pageable);

    @Query(value = "SELECT * FROM contacts WHERE  " +
            "        user_id = :userId AND "+

            "       (LOWER(first_name)   like :search% " +
            "     or LOWER(last_name)    like :search% " +
            "     or LOWER(email)        like %:search2% " +
            "     or LOWER(phone_number) like %:search2%)" +

            "     order by  (LOWER(first_name) like :search% or LOWER(last_name) like :search%) desc nulls last," +
            "                COALESCE(first_name, last_name, email, phone_number)",
            nativeQuery = true)

    Page<Contact> searchContactsByUser(@Param("userId") int userId,
                                       @Param("search") String search,
                                       @Param("search2") String search2,
                                       Pageable pageable);


    @Transactional
    @Modifying
    @Query(value = "UPDATE contacts SET type_id = NULL WHERE type_id = :id", nativeQuery = true)
    void setTypeIdToNull(@Param("id") int x);




}

