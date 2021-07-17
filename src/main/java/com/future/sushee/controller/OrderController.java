package com.future.sushee.controller;

import com.future.sushee.model.Order;
import com.future.sushee.model.Reservation;
import com.future.sushee.payload.request.OrderCreationRequest;
import com.future.sushee.payload.response.MessageResponse;
import com.future.sushee.payload.response.OrderResponse;
import com.future.sushee.repository.OrderRepository;
import com.future.sushee.service.MenuService;
import com.future.sushee.service.OrderService;
import com.future.sushee.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@RestController
@RequestMapping("/api/order")
@CrossOrigin(origins = "*", maxAge = 3600)
public class OrderController {
    
    private final OrderService orderService;
    private final ReservationService reservationService;
    private final MenuService menuService;
    private final OrderRepository orderRepository;

    @GetMapping("")
    public List<OrderResponse> getAllOrder() {
        List<Order> orders =  orderService.getAllOrder();
        List<OrderResponse> response = new ArrayList<>();
        for(Order order: orders) {
            response.add(orderService.createOrderResponse(order));
        }
        return response;
    }

    @GetMapping("/{id}")
    public OrderResponse getOrderById(@PathVariable Long id) {
        try {
            Order order =  orderService.getById(id);
            return orderService.createOrderResponse(order);

        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No such order with ID " + String.valueOf(id)
            );
        }
    }

    @GetMapping("/reservation/{id}")
    public List<OrderResponse> getOrdersByReservationId(@PathVariable Long id) {
        try {
            List<Order> orders = orderService.getOrdersByReservationId(id);
            List<OrderResponse> response = new ArrayList<>();
            for (Order order : orders) {
                response.add(orderService.createOrderResponse(order));
            }
            return response;
        }

        catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No such order with ID " + String.valueOf(id)
            );
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addOrder(@Valid @RequestBody OrderCreationRequest orderCreationRequest) {
        Order order = new Order();
        Reservation reservation = reservationService.getById(orderCreationRequest.getReservationId());
        if (reservation.getStatus()!=1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Reservation not started yet"
            );
        }

        order.setAmount(orderCreationRequest.getAmount());
        order.setStatus(orderCreationRequest.getStatus());
        order.setReservation(reservation);
        order.setMenu(menuService.getById(orderCreationRequest.getMenuId()));

        orderService.add(order);
        return ResponseEntity.ok().body(new MessageResponse("Order successfully added."));
    }

    @PostMapping("/{id}/done")
    public ResponseEntity<?> setDone(@PathVariable Long id) {
        Order order = orderService.getById(id);
        order.setStatus(1);
        orderRepository.save(order);
        return ResponseEntity.ok().body(new MessageResponse("Order status: DONE"));
    }

    @PostMapping("/{id}/pending")
    public ResponseEntity<?> setPending(@PathVariable Long id) {
        Order order = orderService.getById(id);
        order.setStatus(0);
        orderRepository.save(order);
        return ResponseEntity.ok().body(new MessageResponse("Order status: PENDING"));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> setCancel(@PathVariable Long id) {
        Order order = orderService.getById(id);
        order.setStatus(-1);
        orderRepository.save(order);
        return ResponseEntity.ok().body(new MessageResponse("Order status: CANCELLED"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        orderService.delete(orderService.getById(id));
        return ResponseEntity.ok(new MessageResponse("Order " + String.valueOf(id) + " successfully deleted"));
    }
}
