package com.future.sushee.service;

import com.future.sushee.model.Menu;
import com.future.sushee.model.Order;
import com.future.sushee.model.Reservation;
import com.future.sushee.payload.request.OrderCreationRequest;
import com.future.sushee.payload.response.OrderResponse;
import com.future.sushee.repository.OrderRepository;
import com.future.sushee.service.implementations.OrderServiceImpl;
import com.future.sushee.service.interfaces.MenuService;
import com.future.sushee.service.interfaces.ReservationService;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@Tag("service")
public class OrderServiceTest {
    @InjectMocks
    @Spy
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ReservationService reservationService;

    @Mock
    private MenuService menuService;

    @Captor
    ArgumentCaptor<Order> orderArgumentCaptor;

    private List<Order> orders;
    private Order order;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        Order order1 = new Order();
        Order order2 = new Order();
        this.order = new Order();
        this.orders = Arrays.asList(order1, order2);
    }

    @AfterEach()
    public void afterEach() {
//        reset(menu, orderService);
    }
    
    @Test
    public void getAllOrderTest() {
        when(orderRepository.findAll()).thenReturn(orders);
        List<Order> result = orderService.getAllOrder();
        verify(orderRepository).findAll();

        assertEquals(result.size(), 2);
        assertEquals(result.get(0), orders.get(0));
        assertEquals(result.get(1), orders.get(1));
    }

    @Test
    public void getOrderByReservationIdTest() {
        Long id = 1L;
        Reservation reservation1 = new Reservation();
        reservation1.setId(id);
        Reservation reservation2 = new Reservation();
        reservation2.setId(2L);

        orders.get(0).setReservation(reservation1);
        orders.get(1).setReservation(reservation2);
        doReturn(orders).when(orderService).getAllOrder();
        List<Order> result = orderService.getOrdersByReservationId(id);

        verify(orderService).getOrdersByReservationId(id);
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getReservation().getId(), id);
    }

    @Test
    public void getOrderByIdTest() {
        Long id = 1L;
        order.setId(id);

        when(orderRepository.findById(id)).thenReturn(Optional.of(order));
        Order result = orderService.getById(id);

        verify(orderRepository).findById(id);
        assertEquals(result.getId(), id);
    }

    @Test
    public void addOrderTest() {
        Order result = orderService.addOrder(order);
        verify(orderRepository).save(orderArgumentCaptor.capture());

        assertEquals(result, order);
        assertEquals(orderArgumentCaptor.getValue(), order);
    }

    @Test
    public void createOrderResponseTest() {
        Reservation reservation = new Reservation();
        Menu menu = new Menu();
        reservation.setId(1L);
        menu.setId(1L);
        order.setReservation(reservation);
        order.setMenu(menu);

        OrderResponse response = orderService.createOrderResponse(order);
        assertEquals(response.getId(), order.getId());
        assertEquals(response.getAmount(), order.getAmount());
        assertEquals(response.getMenu(), order.getMenu().getId());
        assertEquals(response.getReservation(), order.getReservation().getId());
        assertEquals(response.getStatus(), order.getStatus());
    }

    @Test
    public void addOrderFromRequestTest() {
        OrderCreationRequest orderCreationRequest = new OrderCreationRequest();
        orderCreationRequest.setAmount(10000);
        orderCreationRequest.setMenuId(1L);
        orderCreationRequest.setReservationId(2L);
        orderCreationRequest.setStatus(0);

        Reservation reservation = new Reservation();
        reservation.setStatus(1);
        reservation.setId(2L);

        doReturn(order).when(orderService).addOrder(order);
        when(menuService.getById(1L)).thenReturn(new Menu());
        when(reservationService.getById(2L)).thenReturn(reservation);
        Order result = orderService.addOrderFromRequest(orderCreationRequest);

        verify(menuService).getById(1L);
        verify(reservationService).getById(2L);
        assertEquals(result.getStatus(), orderCreationRequest.getStatus());
        assertEquals(result.getAmount(), orderCreationRequest.getAmount());
    }

    @Test
    public void updateStatusTest() {
        when(orderRepository.save(order)).thenReturn(order);
        doReturn(order).when(orderService).getById(ArgumentMatchers.anyLong());
        Order result = orderService.updateStatus(1L, 1);

        verify(orderRepository).save(order);
        verify(orderService).getById(1L);
        assertEquals(result.getStatus(),1);
    }

    @Test
    public void deleteTest() {
        Order deletedOrder = orders.get(0);
        Order result = orderService.delete(deletedOrder);
        assertEquals(deletedOrder, result);
    }
}
