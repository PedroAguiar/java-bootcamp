package com.globant.util;

import com.globant.dto.ClientDTO;
import com.globant.dto.ItemDTO;
import com.globant.dto.OrderDTO;
import com.globant.model.Client;
import com.globant.model.Item;
import com.globant.model.Order;

import java.util.function.Function;

public final class ModelUtils {

    public static Function<ClientDTO, Client> toClient = clientDTO -> Client.builder()
            .name(clientDTO.getName())
            .lastName(clientDTO.getLastName())
            .description(clientDTO.getDescription())
            .build();

    public static Item toItem(ItemDTO itemDTO) {
        return Item.builder()
                .name(itemDTO.getName())
                .build();
    }

    public static Order toOrder(OrderDTO orderDTO) {
        return Order.builder()
                .build();
    }
}
