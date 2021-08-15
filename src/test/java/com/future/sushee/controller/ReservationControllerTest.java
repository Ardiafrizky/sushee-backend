package com.future.sushee.controller;

import com.future.sushee.model.Reservation;
import com.future.sushee.model.Seat;
import com.future.sushee.model.User;
import com.future.sushee.payload.response.ReservationResponse;
import com.future.sushee.service.implementations.EmailServiceImpl;
import com.future.sushee.service.implementations.ReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("controller")
@SpringBootTest
@AutoConfigureMockMvc
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationServiceImpl reservationService;
    
    @MockBean
    private EmailServiceImpl emailService;

    private User user1;
    private Seat seat1;

    private Reservation reservation1;
    private Reservation reservation2;
    private List<Reservation> reservations;

    private ReservationResponse reservationResponse1;
    private ReservationResponse reservationResponse2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        this.seat1 = new Seat();
        this.user1 = new User("uname1", "fname1", "email1", "pass1");
        this.reservation1 = new Reservation(1L, 1, 10000, LocalDateTime.now(), 1, null, user1, seat1);
        this.reservation2 = new Reservation(2L, 1, 10000, LocalDateTime.now(), 1, null, user1, seat1);
        this.reservationResponse1 = new ReservationResponse(1L, 1, 10000, LocalDateTime.now(), 1, "1", 1L);
        this.reservationResponse2 = new ReservationResponse(2L, 1, 10000, LocalDateTime.now(), 1, "1", 1L);
        this.reservations = Arrays.asList(reservation1, reservation2);
    }
    
    @Test
    public void getAllReservationTest() throws Exception {
        when(reservationService.getAllReservation()).thenReturn(reservations);
        when(reservationService.createReservationResponse(reservation1)).thenReturn(reservationResponse1);
        when(reservationService.createReservationResponse(reservation2)).thenReturn(reservationResponse2);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/reservation")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    public void getReservationByUserTest() throws Exception {
        when(reservationService.getReservationByUsername("uname1")).thenReturn(reservations);
        when(reservationService.createReservationResponse(reservation1)).thenReturn(reservationResponse1);
        when(reservationService.createReservationResponse(reservation2)).thenReturn(reservationResponse2);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/reservation/user/uname1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)));
    }

    @Test
    public void getReservationByIdTest() throws Exception {
        when(reservationService.getById(1L)).thenReturn(reservation1);
        when(reservationService.createReservationResponse(reservation1)).thenReturn(reservationResponse1);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/reservation/id/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void activateReservationTest() throws Exception {
        when(reservationService.getById(1L)).thenReturn(reservation1);
        when(reservationService.activate(reservation1)).thenReturn("OK");

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/reservation/1/update-status")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("OK")));
    }

//    @Test
//    public void addReservationTest() throws Exception {
//        when(reservationService.d(1L)).thenReturn(reservation1);
//        when(reservationService.activate(reservation1)).thenReturn("OK");
//
//        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/reservation/add")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message", is("OK")));
//    }

}
