package com.globant.controller;

import com.globant.aspect.annotation.Timer;
import com.globant.dto.PaymentDTO;
import com.globant.model.Payment;
import com.globant.service.OrderService;
import com.globant.service.PaymentService;
import com.globant.util.DTOUtils;
import com.globant.util.EncryptingUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(value = "payment-service", tags = {"PaymentController"})
@Slf4j
@RequestMapping("/payment")
@RestController
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderService orderService;


    @Timer
    @ApiOperation(value = "Create a payment", response = PaymentDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created payment"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")}
    )
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

    @Timer
    @ApiOperation(value = "Retrieve a payment", response = PaymentDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved payment"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")}
    )
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PaymentDTO> getPayment(@PathVariable(name = "paymentId") String paymentId) throws Exception {
        if (log.isDebugEnabled())
            log.debug("Received GET payment request for payment {}", paymentId);

        final Payment payment = paymentService.getPayment(paymentId);

        log.info("Returning payment {} ", paymentId);

        return new ResponseEntity<>(DTOUtils.toPaymentDTO(payment), HttpStatus.OK);
    }

    @Timer
    @ApiOperation(value = "Retrieve multiple payments", response = PaymentDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved payments"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")}
    )
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

    @Timer
    @ApiOperation(value = "Update payment", response = HttpStatus.class)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Successfully updated payment"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")}
    )
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

    @Timer
    @ApiOperation(value = "Update payment", response = HttpStatus.class)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Successfully updated payment"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")}
    )
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
