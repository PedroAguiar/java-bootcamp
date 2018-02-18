package com.globant.repository;

import com.globant.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

@Transactional
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("UPDATE Payment SET amount = :amount WHERE id = :paymentId")
    @Modifying
    Payment updatePaymentAmount(@Param(value = "paymentId") long paymentId, @Param(value = "amount") long amount) throws Exception;

}
