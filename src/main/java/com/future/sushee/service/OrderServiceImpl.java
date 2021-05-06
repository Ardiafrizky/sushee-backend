package com.future.sushee.service;

import com.future.sushee.model.Order;
import com.future.sushee.model.Reservation;
import com.future.sushee.repository.OrderRepository;
import com.future.sushee.repository.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public List<Order> getAllOrder() { return orderRepository.findAll(); }

    @Override
    public List<Order> getOrdersByReservationId(Long id) {
        List<Order> result = new ArrayList<>();

        for(Order order : getAllOrder()) {
            if(order.getReservation().getId().equals(id)) { result.add(order); }
        }
        return result;
    }

    @Override
    public Order add(Order order) {
        orderRepository.save(order);
        return order;
    }

    @Override
    public Order getById(Long id) { return orderRepository.findById(id).get(); }

    @Override
    public Order delete(Order order) {
        orderRepository.delete(order);
        return order;
    }
}
