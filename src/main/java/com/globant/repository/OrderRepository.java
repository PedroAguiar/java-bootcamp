package com.globant.repository;

import com.globant.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface OrderRepository extends JpaRepository<Order, Long> {
}
