package com.globant.service.impl;

import com.globant.dto.OrderDTO;
import com.globant.model.Item;
import com.globant.model.Order;
import com.globant.repository.ItemRepository;
import com.globant.repository.OrderRepository;
import com.globant.service.OrderService;
import com.globant.util.EncryptingUtil;
import com.globant.util.ModelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    @Autowired
    private OrderServiceImpl(OrderRepository orderRepository, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public Order createOrder(OrderDTO orderDTO) throws Exception {
        Validate.notNull(orderDTO);
        Validate.notEmpty(orderDTO.getItemIds());
        if(log.isDebugEnabled())
            log.debug("Creating order with items {} ", orderDTO.getItemIds());

        Order order = ModelUtils.toOrder(orderDTO);

        final List<Item> items = orderDTO.getItemIds().stream()
                .map(itemId -> itemRepository.getOne(EncryptingUtil.decryptId(itemId)))
                .collect(Collectors.toList());
        order.setItems(items);

        return orderRepository.save(order);
    }

    @Override
    public List<Order> getOrders(List<String> orderIds) {
        Validate.notEmpty(orderIds);
        final List<Long> decryptedOrderIds = EncryptingUtil.decryptStrings(orderIds)
                .collect(Collectors.toList());
        return orderRepository.findAll(decryptedOrderIds);
    }

    @Override
    public Order getOrder(String orderId) {
        return orderRepository.getOne(EncryptingUtil.decryptId(orderId));
    }

    @Override
    public Order updateOrder(OrderDTO orderDTO) throws Exception {
        Validate.notNull(orderDTO);
        Validate.notEmpty(orderDTO.getItemIds());

        Order order =  getOrder(orderDTO.getOrderId());
        List<Long> itemIds = EncryptingUtil.decryptStrings(orderDTO.getItemIds()).collect(Collectors.toList());
        List<Item> items = itemRepository.findAll(itemIds);

        if (items.isEmpty()) {
            order.getItems().addAll(items);
        }

        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(String orderId) throws Exception {
        orderRepository.delete(EncryptingUtil.decryptId(orderId));
    }
}
