package com.future.sushee.controller;

import com.future.sushee.model.Order;
import com.future.sushee.model.Reservation;
import com.future.sushee.payload.request.OrderCreationRequest;
import com.future.sushee.payload.response.MessageResponse;
import com.future.sushee.service.MenuService;
import com.future.sushee.service.OrderService;
import com.future.sushee.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
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

    @GetMapping("")
    public List<Order> getAllOrder() {
        return orderService.getAllOrder();
    }

    // TODO: Optimize order GET-JSON

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        try {
            return orderService.getById(id);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No such order with ID " + String.valueOf(id)
            );
        }
    }

    @GetMapping("/reservation/{id}")
    public List<Order> getOrdersByReservationId(@PathVariable Long id) {
        return orderService.getOrdersByReservationId(id);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addOrder(@Valid @RequestBody OrderCreationRequest orderCreationRequest) {

        Order order = new Order();

        order.setAmount(orderCreationRequest.getAmount());
        order.setStatus(orderCreationRequest.getStatus());
        order.setReservation(reservationService.getById(orderCreationRequest.getReservationId()));
        order.setMenu(menuService.getById(orderCreationRequest.getMenuId()));

        orderService.add(order);
        return ResponseEntity.ok().body(new MessageResponse("Order successfully added."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        orderService.delete(orderService.getById(id));
        return ResponseEntity.ok(new MessageResponse("Order " + String.valueOf(id) + " successfully deleted"));
    }
}
