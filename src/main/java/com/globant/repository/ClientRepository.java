package com.globant.repository;

import com.globant.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

@Transactional
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("SELECT c FROM Client c WHERE c.name = :name OR c.lastName = :lastName")
    Client findByNameAndLastName(@Param("name") String name, @Param("lastName") String lastName);


}
