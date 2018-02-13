package com.globant.repository;

import com.globant.model.Client;
import com.globant.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("SELECT c FROM Client c WHERE c.name = :name OR c.lastName = :lastName")
    Client findByNameAndLastName(@Param("name") String name, @Param("lastName") String lastName);


}
