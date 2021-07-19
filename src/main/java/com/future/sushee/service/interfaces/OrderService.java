package com.future.sushee.service.interfaces;

import com.future.sushee.model.Order;
import com.future.sushee.payload.request.OrderCreationRequest;
import com.future.sushee.payload.response.OrderResponse;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> getAllOrder();
    List<Order> getOrdersByReservationId(Long id);
    OrderResponse createOrderResponse(Order order);
    Order getById(Long id);
    Order addOrder(Order order);
    Order addOrderFromRequest(OrderCreationRequest orderCreationRequest);
    Order updateStatus(Long id, Integer status);
    Order delete(Order order);
}
