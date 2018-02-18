package com.globant.controller.api;

import com.globant.controller.ClientController;
import com.globant.controller.ItemController;
import com.globant.controller.OrderController;
import com.globant.controller.PaymentController;
import com.globant.dto.ClientDTO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Api
@RestController
public class Gateway {

    @Autowired
    private ItemController itemController;

    @Autowired
    private OrderController orderController;

    @Autowired
    private ClientController clientController;

    @Autowired
    private PaymentController paymentController;


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
