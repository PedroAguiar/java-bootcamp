package com.globant.controller;

import com.globant.dto.PaymentDTO;
import com.globant.model.Payment;
import com.globant.service.OrderService;
import com.globant.service.PaymentService;
import com.globant.util.DTOUtils;
import com.globant.util.EncryptingUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Api
@Slf4j
@RequestMapping("/payment")
@RestController
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderService orderService;


    @PutMapping(path = "/{clientId}/{orderId}/{amount}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentDTO> createPayment(@PathVariable(name = "clientId") String clientId,
                                                    @PathVariable(name = "orderId") String orderId,
                                                    @PathVariable(name = "amount") String amount) throws Exception {
        if (log.isDebugEnabled())
            log.debug("Received CREATE payment request for client {} and order {}", clientId, orderId);

        Validate.notBlank(clientId, orderId, amount);
        final Payment payment = paymentService.createPayment(DTOUtils.toPaymentDTO("", orderId, amount));

        log.info("Created payment {} for client {}", payment.getId(), clientId);

        if (log.isDebugEnabled())
            log.debug("Returning payment {} for client {}", payment.getId(), clientId);

        return new ResponseEntity<>(DTOUtils.toPaymentDTO(payment), HttpStatus.CREATED);
    }


    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentDTO> getPayment(@PathVariable(name = "paymentId") String paymentId) throws Exception {
        if (log.isDebugEnabled())
            log.debug("Received GET payment request for payment {}", paymentId);

        final Payment payment = paymentService.getPayment(paymentId);

        log.info("Returning payment {} ", paymentId);

        return new ResponseEntity<>(DTOUtils.toPaymentDTO(payment), HttpStatus.OK);
    }


    @GetMapping(path = "/{clientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus getPayments(@PathVariable(name = "clientId") String clientId) throws Exception {
        if (log.isDebugEnabled())
            log.debug("Received GET payments request for client {}", clientId);

        final List<Payment> payments = paymentService.getPayments(clientId);
        final List<String> paymentIds = payments.stream()
                .map(Payment::getId)
                .map(String::valueOf)
                .map(EncryptingUtil::encryptString)
                .collect(Collectors.toList());

        if (log.isDebugEnabled())
            log.debug("Returning payments {} for client {}", paymentIds.toArray(), clientId);

        return HttpStatus.ACCEPTED;

    }


    @PostMapping(path = "/{paymentId}/{amount}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus updatePayment(@PathVariable(name = "paymentId") String paymentId,
                                    @PathVariable(name = "amount") String amount) throws Exception {
        if (log.isDebugEnabled())
            log.debug("Received UPDATE payment request for payment {}", paymentId);

        validate(paymentId);

        final Payment payment = paymentService.updatePayment(paymentId, amount);

        log.info("Updated payment {}", paymentId);

        if (log.isDebugEnabled())
            log.debug("Updated payment {} data to: ", payment.toString());

        return HttpStatus.ACCEPTED;
    }


    @DeleteMapping(path = "/{paymentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus deletePayment(@PathVariable(name = "paymentId") String paymentId) throws Exception {
        if (log.isDebugEnabled())
            log.debug("Received DELETE payment request for payment {}", paymentId);

        paymentService.deletePayment(paymentId);

        if (log.isDebugEnabled())
            log.debug("Deleted payment {} {}", paymentId);

        return HttpStatus.ACCEPTED;
    }

    private void validate(String paymentId) throws Exception {
        Validate.notNull(paymentService.getPayment(paymentId));
    }
}
