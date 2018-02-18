package com.globant.repository;

import com.globant.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    @Query("SELECT c FROM Client c WHERE c.name = :name OR c.lastName = :lastName")
    List<Client> findByNameAndLastName(@Param("name") String name, @Param("lastName") String lastName);


}
