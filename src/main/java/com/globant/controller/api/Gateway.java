package com.globant.controller.api;

import com.globant.aspect.annotation.Log;
import com.globant.aspect.annotation.Timer;
import com.globant.controller.ClientController;
import com.globant.controller.ItemController;
import com.globant.controller.OrderController;
import com.globant.controller.PaymentController;
import com.globant.dto.ClientDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api(value = "payment-service", tags = {"API Gateway"})
@RestController
@RequestMapping(value = "/v1")
public class Gateway {

    private final ItemController itemController;
    private final OrderController orderController;
    private final ClientController clientController;
    private final PaymentController paymentController;

    @Autowired
    public Gateway(ItemController itemController, OrderController orderController, ClientController clientController, PaymentController paymentController) {
        this.itemController = itemController;
        this.orderController = orderController;
        this.clientController = clientController;
        this.paymentController = paymentController;
    }


    @Log
    @Timer
    @ApiOperation(value = "Commit payment transaction", response = PaymentResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully committed payment transaction"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")}
    )
    @PostMapping(path = "/pay")
    public ResponseEntity<PaymentResponse> pay(@RequestBody PaymentRequest paymentRequest) throws Exception {
        final List<String> itemIds = new ArrayList<>();
        ResponseEntity<ClientDTO> clientDTO = clientController.createClient(paymentRequest.getClientName(), paymentRequest.getClientLastName(), paymentRequest.getClientDescription());
        ClientDTO clientDTO1 = clientDTO.getBody();

        for (int i = 0; i < paymentRequest.getItemNames().size(); i++) {
            itemIds.add(itemController.createItem(paymentRequest.getItemNames().get(i)).getBody().getId());
        }

        String orderId = orderController.createOrder(itemIds).getBody().getOrderId();
        String paymentId = paymentController.createPayment(clientDTO1.getClientId(), orderId, paymentRequest.getPaymentAmount()).getBody().getId();

        return new ResponseEntity<>(new PaymentResponse(paymentId), HttpStatus.CREATED);

    }
}
