package com.globant.controller;

import com.globant.aspect.annotation.Log;
import com.globant.aspect.annotation.Timer;
import com.globant.dto.OrderDTO;
import com.globant.model.Order;
import com.globant.service.OrderService;
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

@Api(value = "payment-service", tags = {"OrderController"})
@Slf4j
@RequestMapping(value = "/order")
@RestController
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Log
    @Timer
    @ApiOperation(value = "Create an order", response = OrderDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created order"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")}
    )
    @PutMapping(path = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDTO> createOrder(@RequestParam(name = "itemIds") List<String> itemIds) throws Exception {

        Validate.notEmpty(itemIds);
        final Order order = orderService.createOrder(DTOUtils.toOrderDTO("", itemIds));
        log.info("Created order {}", order.getId());

        return new ResponseEntity<>(DTOUtils.toOrderDTO(order), HttpStatus.CREATED);
    }

    @Log
    @Timer
    @ApiOperation(value = "Retrieve an order", response = OrderDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved order"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")}
    )
    @GetMapping(path = "/retrieve", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDTO> getOrder(@RequestParam(name = "orderId") String orderId) throws Exception {

        final Order order = orderService.getOrder(orderId);
        log.info("Returning order {} ", orderId);

        return new ResponseEntity<>(DTOUtils.toOrderDTO(order), HttpStatus.OK);
    }

    @Log
    @Timer
    @ApiOperation(value = "Update an order", response = OrderDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Successfully updated order"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")}
    )
    @PostMapping(path = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus updateOrder(@RequestParam(name = "orderId") String orderId,
                                  @RequestParam(name = "itemIds") List<String> itemIds) throws Exception {

        validate(orderId);
        orderService.updateOrder(DTOUtils.toOrderDTO(orderId, itemIds));
        log.info("Updated order {}", orderId);

        return HttpStatus.ACCEPTED;
    }

    @Log
    @Timer
    @ApiOperation(value = "Delete an order", response = OrderDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Successfully deleted order"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")}
    )
    @DeleteMapping(path = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus deleteOrder(@RequestParam(name = "orderId") String orderId) throws Exception {

        validate(orderId);
        orderService.deleteOrder(orderId);
        log.info("Deleted order {}", orderId);

        return HttpStatus.ACCEPTED;
    }

    private void validate(String orderId) throws Exception {
        Validate.notNull(orderService.getOrder(orderId));
    }
}
