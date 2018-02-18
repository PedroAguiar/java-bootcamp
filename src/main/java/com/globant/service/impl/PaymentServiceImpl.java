package com.globant.service.impl;

import com.globant.dto.PaymentDTO;
import com.globant.model.Order;
import com.globant.model.Payment;
import com.globant.repository.OrderRepository;
import com.globant.repository.PaymentRepository;
import com.globant.service.ClientService;
import com.globant.service.PaymentService;
import com.globant.util.EncryptingUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    private final ClientService clientService;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public PaymentServiceImpl(ClientService clientService, PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.clientService = clientService;
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Payment createPayment(PaymentDTO paymentDTO) throws Exception {
        Validate.notNull(paymentDTO);
        final Order order = orderRepository.getOne(EncryptingUtil.decryptId(paymentDTO.getOrderId()));
        final Payment payment = Payment.builder()
                .order(order)
                .amount(Long.valueOf(paymentDTO.getAmount()))
                .build();
        return paymentRepository.save(payment);
    }

    @Override
    public List<Payment> getPayments(String clientId) throws Exception {
        return clientService.getClient(clientId).getPayments();
    }

    @Override
    public Payment getPayment(String paymentId) throws Exception {
        return paymentRepository.getOne(Long.valueOf(paymentId));
    }

    @Override
    public Payment updatePayment(String paymentId, String amount) throws Exception {
        Validate.notBlank(paymentId, amount);
        if (log.isDebugEnabled())
            log.debug("Trying to update payment {} amount to {} ", paymentId, amount);
        paymentRepository.getOne(Long.valueOf(paymentId));
        return paymentRepository.updatePaymentAmount(Long.valueOf(paymentId), Long.valueOf(amount));
    }

    @Override
    public void deletePayment(String paymentId) throws Exception {

        if (log.isDebugEnabled())
            log.debug("Trying to delete payment {} ", paymentId);

        paymentRepository.delete(Long.valueOf(paymentId));
    }
}
