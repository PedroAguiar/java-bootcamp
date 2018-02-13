package com.globant.controller;

import com.globant.dto.OrderDTO;
import com.globant.model.Order;
import com.globant.service.OrderService;
import com.globant.util.DTOUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@Slf4j
@RequestMapping(value = "/order")
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PutMapping(path = "/{itemIds}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDTO> createOrder(@PathVariable(name = "itemIds") List<String> itemIds) throws Exception {

        if (log.isDebugEnabled())
            log.debug("Received CREATE order request with items {}" , itemIds);

        Validate.notEmpty(itemIds);
        final Order order = orderService.createOrder(DTOUtils.toOrderDTO("", itemIds));

        log.info("Created order {}", order.getId());

        if (log.isDebugEnabled())
            log.debug("Returning order {}", order.toString());

        return new ResponseEntity<>(DTOUtils.toOrderDTO(order), HttpStatus.CREATED);
    }


    @GetMapping(path = "/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDTO> getOrder(@PathVariable(name = "orderId") String orderId) throws Exception {
        if (log.isDebugEnabled())
            log.debug("Received GET order request for order {}", orderId);

        final Order order = orderService.getOrder(orderId);

        log.info("Returning order {} ", orderId);

        return new ResponseEntity<>(DTOUtils.toOrderDTO(order), HttpStatus.OK);
    }


    @PostMapping(path = "/{orderId}/{itemIds}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus updateOrder(@PathVariable(name = "orderId") String orderId,
                                  @PathVariable(name = "itemIds") List<String> itemIds) throws Exception {
        if (log.isDebugEnabled())
            log.debug("Received UPDATE order request for order {}", orderId);

        validate(orderId);
        Order item = orderService.updateOrder(DTOUtils.toOrderDTO(orderId, itemIds));

        log.info("Updated order {}", orderId);

        if (log.isDebugEnabled())
            log.debug("Updated order {} data to: ", item.toString());

        return HttpStatus.ACCEPTED;
    }


    @DeleteMapping(path = "/{orderId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus deleteOrder(@PathVariable(name = "orderId") String orderId) throws Exception {
        if (log.isDebugEnabled())
            log.debug("Received DELETE item request for item {}", orderId);

        validate(orderId);
        orderService.deleteOrder(orderId);

        log.info("Deleted order {}", orderId);

        return HttpStatus.ACCEPTED;
    }

    private void validate(String orderId) throws Exception {
        Validate.notNull(orderService.getOrder(orderId));
    }
}
