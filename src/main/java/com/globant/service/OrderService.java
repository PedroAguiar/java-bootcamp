package com.globant.service;

import com.globant.dto.OrderDTO;
import com.globant.model.Order;

import java.util.List;

public interface OrderService {

    Order createOrder(OrderDTO orderDTO) throws Exception;

    List<Order> getOrders(List<String> clientId) throws Exception;

    Order getOrder(String orderId);

    Order updateOrder(OrderDTO orderDTO) throws Exception;

    void deleteOrder(String orderId) throws Exception;
}
