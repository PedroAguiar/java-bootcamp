package com.globant.util;

import com.globant.dto.ClientDTO;
import com.globant.dto.ItemDTO;
import com.globant.dto.OrderDTO;
import com.globant.dto.PaymentDTO;
import com.globant.model.Client;
import com.globant.model.Item;
import com.globant.model.Order;
import com.globant.model.Payment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class DTOUtils {

    public static ClientDTO toClientDTO (String clientId, String name, String lastName, String description) {
        return ClientDTO.builder()
                .clientId(clientId)
                .name(name)
                .lastName(lastName)
                .description(description)
                .build();
    }

    public static ClientDTO toClientDTO(Client client) {
        List<String> paymentIds = new ArrayList<>();
        if (client.getPayments() != null) {
             paymentIds = client.getPayments().stream()
                    .map(Payment::getId)
                    .map(String::valueOf)
                    .map(EncryptingUtil::encryptString)
                    .collect(Collectors.toList());
        }


        return ClientDTO.builder()
                .clientId(EncryptingUtil.encryptString(String.valueOf(client.getId())))
                .name(client.getName())
                .lastName(client.getLastName())
                .description(client.getDescription())
                .paymentIds(paymentIds)
                .build();
    }

    public static ItemDTO toItemDTO(String id, String name) {
        return ItemDTO.builder()
                .id(id)
                .name(name)
                .build();
    }

    public static ItemDTO toItemDTO(Item item) {
        return ItemDTO.builder()
                .id(EncryptingUtil.encryptString(String.valueOf(item.getId())))
                .name(item.getName())
                .build();
    }


    public static OrderDTO toOrderDTO(String orderId, List<String> itemIds) {
        return OrderDTO.builder()
                .orderId(orderId)
                .itemIds(itemIds)
                .build();
    }

    public static OrderDTO toOrderDTO(Order order) {
        final List<String> itemIds = order.getItems().stream()
                .map(Item::getId)
                .map(String::valueOf)
                .map(EncryptingUtil::encryptString)
                .collect(Collectors.toList());

        return OrderDTO.builder()
                .orderId(EncryptingUtil.encryptString(String.valueOf(order.getId())))
                .itemIds(itemIds)
                .build();
    }

    public static PaymentDTO toPaymentDTO(String paymentId, String orderId, String amount) {
        return PaymentDTO.builder()
                .id(paymentId)
                .orderId(orderId)
                .amount(amount)
                .build();
    }

    public static PaymentDTO toPaymentDTO(Payment payment) {
        return PaymentDTO.builder()
                .id(EncryptingUtil.encryptString(String.valueOf(payment.getId())))
                .orderId(EncryptingUtil.encryptString(String.valueOf(payment.getOrder().getId())))
                .amount(String.valueOf(payment.getAmount()))
                .build();
    }
}
