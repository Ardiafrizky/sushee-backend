package com.future.sushee.service;

import com.future.sushee.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> getAllOrder();
    Order add(Order order);
    Order delete(Order order);
}
