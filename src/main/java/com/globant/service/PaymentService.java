package com.globant.service;

import com.globant.dto.PaymentDTO;
import com.globant.model.Payment;

import java.util.List;

public interface PaymentService {

    Payment createPayment(PaymentDTO paymentDTO) throws Exception;

    List<Payment> getPayments(String clientId) throws Exception;

    List<Payment> getPayments() throws Exception;

    Payment getPayment(String paymentId) throws Exception;

    Payment updatePayment(String paymentId, String amount) throws Exception;

    void deletePayment(String paymentId) throws Exception;

}
