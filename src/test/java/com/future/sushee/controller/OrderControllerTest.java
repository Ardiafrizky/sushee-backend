package com.future.sushee.controller;

import com.future.sushee.model.Menu;
import com.future.sushee.model.Order;
import com.future.sushee.model.Reservation;
import com.future.sushee.payload.request.OrderCreationRequest;
import com.future.sushee.payload.response.OrderResponse;
import com.future.sushee.service.implementations.OrderServiceImpl;
import com.future.sushee.service.interfaces.OrderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.MockAnnotationProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("controller")
@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderServiceImpl orderService;

    private Order order1;
    private Order order2;
    private List<Order> orders;

    private OrderCreationRequest orderCreationRequest;
    private OrderResponse orderResponse1;
    private OrderResponse orderResponse2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        this.order1 = new Order(1L, 1, 0, new Menu(), new Reservation());
        this.order2 = new Order(2L, 2, 0, new Menu(), new Reservation());
        this.orders = Arrays.asList(order1, order2);

        this.orderCreationRequest = new OrderCreationRequest(1, 0, 1L, 1L);
        this.orderResponse1 = new OrderResponse(1L, 1, 0, new Menu(), 1L);
        this.orderResponse2 = new OrderResponse(2L, 1, 0, new Menu(), 1L);
    }

    @Test
    public void getAllOrderTest() throws Exception {
        when(orderService.getAllOrder()).thenReturn(orders);
        when(orderService.createOrderResponse(order1)).thenReturn(orderResponse1);
        when(orderService.createOrderResponse(order2)).thenReturn(orderResponse2);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/order")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    public void getOrderByIdSuccessTest() throws Exception {
        when(orderService.getById(1L)).thenReturn(order1);
        when(orderService.createOrderResponse(order1)).thenReturn(orderResponse1);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/order/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void getOrderByIdNotFoundTest() throws Exception {
        when(orderService.getById(1L)).thenThrow(NoSuchElementException.class);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/order/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getOrderByReservationIdSuccessTest() throws Exception {
        when(orderService.getOrdersByReservationId(1L)).thenReturn(orders);
        when(orderService.createOrderResponse(order1)).thenReturn(orderResponse1);
        when(orderService.createOrderResponse(order2)).thenReturn(orderResponse2);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/order/reservation/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    public void getOrderByReservationIdNotFoundTest() throws Exception {
        when(orderService.getOrdersByReservationId(1L)).thenThrow(NoSuchElementException.class);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/order/reservation/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addOrderTest() throws Exception {
        when(orderService.addOrderFromRequest(orderCreationRequest)).thenReturn(order1);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/order/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"amount\": 1,\n" +
                        "    \"status\": 0,\n" +
                        "    \"reservationId\": 1,\n" +
                        "    \"menuId\": 1\n" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Order successfully added (id: 1).")));
    }

    @Test
    public void setDoneTest() throws Exception {
        when(orderService.updateStatus(1L, 1)).thenReturn(order1);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/order/1/done")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Order 1 status: DONE")));
    }

    @Test
    public void setPendingTest() throws Exception {
        when(orderService.updateStatus(1L, 0)).thenReturn(order1);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/order/1/pending")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Order 1 status: PENDING")));
    }

    @Test
    public void setCancelTest() throws Exception {
        when(orderService.updateStatus(1L, -1)).thenReturn(order1);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/order/1/cancel")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Order 1 status: CANCELLED")));
    }

    @Test
    public void deleteOrderTest() throws Exception {
        when(orderService.getById(1L)).thenReturn(order1);
        when(orderService.delete(order1)).thenReturn(null);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/order/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Order 1 successfully deleted")));
    }
}
