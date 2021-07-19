package com.future.sushee.service.implementations;

import com.future.sushee.model.Order;
import com.future.sushee.model.Reservation;
import com.future.sushee.payload.request.OrderCreationRequest;
import com.future.sushee.payload.response.OrderResponse;
import com.future.sushee.repository.OrderRepository;
import com.future.sushee.service.interfaces.MenuService;
import com.future.sushee.service.interfaces.OrderService;
import com.future.sushee.service.interfaces.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ReservationService reservationService;
    private final MenuService menuService;

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
    public OrderResponse createOrderResponse(Order order){
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setAmount(order.getAmount());
        response.setStatus(order.getStatus());
        response.setMenu(order.getMenu().getId());
        response.setReservation(order.getReservation().getId());

        return response;
    }

    @Override
    public Order addOrder(Order order) {
        orderRepository.save(order);
        return order;
    }

    @Override
    public Order addOrderFromRequest(OrderCreationRequest orderCreationRequest) {
        Order order = new Order();
        Reservation reservation = reservationService.getById(orderCreationRequest.getReservationId());

        if (reservation.getStatus()!=1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Reservation not started yet");
        }
        order.setAmount(orderCreationRequest.getAmount());
        order.setStatus(orderCreationRequest.getStatus());
        order.setReservation(reservation);
        order.setMenu(menuService.getById(orderCreationRequest.getMenuId()));
        return addOrder(order);
    }

    @Override
    public Order getById(Long id) { return orderRepository.findById(id).get(); }

    @Override
    public Order updateStatus(Long id, Integer status) {
        Order order = getById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    @Override
    public Order delete(Order order) {
        orderRepository.delete(order);
        return order;
    }
}
