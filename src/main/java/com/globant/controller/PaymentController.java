package com.globant.controller;

import com.globant.aspect.annotation.Log;
import com.globant.aspect.annotation.Timer;
import com.globant.dto.PaymentDTO;
import com.globant.model.Payment;
import com.globant.service.PaymentService;
import com.globant.util.DTOUtils;
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

@Api(value = "payment-service", tags = {"PaymentController"})
@Slf4j
@RequestMapping("/payment")
@RestController
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @Log
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

        Validate.notBlank(clientId, orderId, amount);
        final Payment payment = paymentService.createPayment(DTOUtils.toPaymentDTO("", orderId, amount));
        log.info("Created payment {} for client {}", payment.getId(), clientId);

        return new ResponseEntity<>(DTOUtils.toPaymentDTO(payment), HttpStatus.CREATED);
    }

    @Log
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

        final Payment payment = paymentService.getPayment(paymentId);
        log.info("Returning payment {} ", paymentId);

        return new ResponseEntity<>(DTOUtils.toPaymentDTO(payment), HttpStatus.OK);
    }

    @Log
    @Timer
    @ApiOperation(value = "Retrieve multiple payments", response = PaymentDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved payments"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")}
    )
    @GetMapping(path = "/{clientId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Payment>> getPayments(@PathVariable(name = "clientId") String clientId) throws Exception {

        final List<Payment> payments = paymentService.getPayments(clientId);

        return new ResponseEntity<>(payments, HttpStatus.OK);
    }

    @Log
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

        validate(paymentId);
        paymentService.updatePayment(paymentId, amount);
        log.info("Updated payment {}", paymentId);

        return HttpStatus.ACCEPTED;
    }

    @Log
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

        paymentService.deletePayment(paymentId);

        return HttpStatus.ACCEPTED;
    }

    private void validate(String paymentId) throws Exception {
        Validate.notNull(paymentService.getPayment(paymentId));
    }
}
