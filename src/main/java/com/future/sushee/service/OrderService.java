package com.future.sushee.service;

import com.future.sushee.model.Order;
import com.future.sushee.payload.response.OrderResponse;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> getAllOrder();
    List<Order> getOrdersByReservationId(Long id);
    OrderResponse createOrderResponse(Order order);
    Order getById(Long id);
    Order add(Order order);
    Order delete(Order order);
}
