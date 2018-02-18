package com.globant.repository;

import com.globant.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface ItemRepository extends JpaRepository<Item, Long> {

}
