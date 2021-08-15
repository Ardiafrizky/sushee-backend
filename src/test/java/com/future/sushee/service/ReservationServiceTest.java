package com.future.sushee.service;

import com.future.sushee.model.Reservation;
import com.future.sushee.model.Seat;
import com.future.sushee.model.User;
import com.future.sushee.payload.request.ReservationCreationRequest;
import com.future.sushee.payload.response.ReservationResponse;
import com.future.sushee.repository.ReservationRepository;
import com.future.sushee.service.implementations.ReservationServiceImpl;
import com.future.sushee.service.interfaces.SeatService;
import com.future.sushee.service.interfaces.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.test.context.annotation.SecurityTestExecutionListeners;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@Tag("service")
public class ReservationServiceTest {

    @Spy
    @InjectMocks
    ReservationServiceImpl reservationService;

    @Mock
    ReservationRepository reservationRepository;

    @Mock
    UserService userService;

    @Mock
    SeatService seatService;

    private Seat seat1;
    private Seat seat2;
    private User user1;
    private User user2;
    private Reservation reservation1;
    private Reservation reservation2;
    private ReservationCreationRequest reservationCreationRequest;
    private List<Reservation> reservations;
    private List<Seat> seats;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        this.user1 = new User(
                "username1", "fullname1", "email1", "password1"
        );
        this.user2 = new User(
                "username2", "fullname2", "email2", "password2"
        );
        this.seat1 = new Seat(
                1L, 2, Boolean.TRUE, null
        );
        this.seat2 = new Seat(
                2L, 4, Boolean.TRUE, null
        );
        this.reservation1 = new Reservation(
                1L, 2, 10000, LocalDateTime.now(), 1, null, user1, seat1
        );
        this.reservation2 = new Reservation(
                2L, 4, 20000, LocalDateTime.now(), 0, null, user2, seat2
        );
        this.reservationCreationRequest = new ReservationCreationRequest(
                "username2", 2L, 4, LocalDateTime.now()
        );

        this.reservations = Arrays.asList(reservation1, reservation2);
        this.seats = Arrays.asList(seat1, seat2);
    }

    @Test
    public void getAllReservationTest() {
        when(reservationRepository.findAll()).thenReturn(reservations);
        List<Reservation> result = reservationService.getAllReservation();
        verify(reservationRepository).findAll();
        assertEquals(result, reservations);
    }

    @Test
    public void getReservationByUsernameTest() {
        String username = "username";
        when(reservationRepository.findByUser(ArgumentMatchers.any())).thenReturn(reservations);
        when(userService.getUserByUsername(ArgumentMatchers.anyString())).thenReturn(user1);

        List<Reservation> result = reservationService.getReservationByUsername(username);
        verify(reservationRepository).findByUser(user1);
        verify(userService).getUserByUsername(username);
        assertTrue(result.contains(reservation1));
    }

    @Test
    public void createReservationResponseTest() {
        ReservationResponse result = reservationService.createReservationResponse(reservation1);
        assertEquals(reservation1.getId(), result.getId());
        assertEquals(reservation1.getNumberOfPerson(), result.getNumberOfPerson());
        assertEquals(seat1.getNumber(), result.getSeat());
        assertEquals(user1.getUuid(), result.getUser());
        assertEquals(reservation1.getStartingDateTime(), result.getStartingDateTime());
        assertEquals(reservation1.getStatus(), result.getStatus());
    }

    @Test
    public void addFromRequestTest() {
        when(seatService.getByNumber(ArgumentMatchers.anyLong())).thenReturn(seat2);
        when(userService.getUserByUsername(ArgumentMatchers.anyString())).thenReturn(user2);
        doReturn(Boolean.TRUE).when(reservationService).checkAvailability(ArgumentMatchers.any());
        doReturn(Boolean.TRUE).when(reservationService).checkSeatValidity(ArgumentMatchers.any());
        doReturn(reservation2).when(reservationService).add(ArgumentMatchers.any());

        Reservation result = reservationService.addFromRequest(reservationCreationRequest);

        verify(seatService).getByNumber(reservationCreationRequest.getSeatNumber());
        verify(userService).getUserByUsername(reservationCreationRequest.getUsername());
        verify(reservationService).checkAvailability(ArgumentMatchers.any());
        verify(reservationService).checkSeatValidity(ArgumentMatchers.any());
        verify(reservationService).add(ArgumentMatchers.any());
        assertEquals(result.getStartingDateTime(), reservationCreationRequest.getStartingDateTime());
        assertEquals(result.getNumberOfPerson(), reservationCreationRequest.getNumberOfPerson());
        assertEquals(result.getUser().getUsername(), reservationCreationRequest.getUsername());
        assertEquals(result.getSeat().getNumber(), reservationCreationRequest.getSeatNumber());
    }

    @Test
    public void addTest() {
        Reservation result = reservationService.add(reservation1);
        verify(reservationRepository).save(reservation1);
    }

    @Test
    public void getByIdTest() {
        Long id = 1L;
        when(reservationRepository.findById(id)).thenReturn(Optional.of(reservation1));

        Reservation result = reservationService.getById(id);
        verify(reservationRepository).findById(id);
        assertEquals(result.getId(), id);
    }

    @Test
    public void deleteByIdTest() {
        Reservation result = reservationService.deleteById(reservation1);
        verify(reservationRepository).delete(reservation1);
    }

    @Test
    public void activateTest() {
        String result = reservationService.activate(reservation2);
        verify(reservationRepository).save(ArgumentMatchers.any());
        assertEquals(result, "The reservation has been activated successfully");

        reservation2.setStartingDateTime(LocalDateTime.now().plusDays(1));
        result = reservationService.activate(reservation2);
        verify(reservationRepository).save(ArgumentMatchers.any());
        assertEquals(result, "The reservation is not started yet");

        reservation2.setStartingDateTime(LocalDateTime.now().minusDays(1));
        result = reservationService.activate(reservation2);
        verify(reservationRepository).save(ArgumentMatchers.any());
        assertEquals(result, "The reservation is expired");
    }


    @Test
    public void checkSeatValidityTest() {
        Boolean result = reservationService.checkSeatValidity(reservation1);
        assertTrue(result);
    }

    @Test
    public void checkAvailabilityTest() {
        doReturn(reservations).when(reservationService).getAllReservation();
        Boolean result = reservationService.checkAvailability(reservation1);
        assertEquals(result, Boolean.FALSE);

        Reservation reservation3 = new Reservation();
        Seat seat3 = new Seat();
        seat3.setNumber(3L);
        reservation3.setSeat(seat3);
        reservation3.setStartingDateTime(LocalDateTime.now());

        result = reservationService.checkAvailability(reservation3);
        verify(reservationService, times(2)).getAllReservation();
        assertEquals(result, Boolean.TRUE);
    }

    @Test
    public void calculatePriceTest() {
        Integer result = reservationService.calculatePrice(10000, 4, 0.1f);
        assertEquals(result, 44000);
    }

    @Test
    public void getAvailableSeatTest() {
        LocalDateTime time = LocalDateTime.now();
        when(seatService.getAllSeat()).thenReturn(seats);
        doReturn(reservations).when(reservationService).getAllReservation();
        Set<Seat> result = reservationService.getAvailableSeats(time);

        verify(seatService, times(2)).getAllSeat();
        verify(reservationService).getAllReservation();
        assertEquals(result.size(), 0);
    }
}
