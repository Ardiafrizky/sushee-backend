package com.future.sushee.controller;

import com.future.sushee.model.Seat;
import com.future.sushee.service.implementations.ReservationServiceImpl;
import com.future.sushee.service.implementations.SeatServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("controller")
@SpringBootTest
@AutoConfigureMockMvc
public class SeatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeatServiceImpl seatService;

    @MockBean
    private ReservationServiceImpl reservationService;

    private Seat seat1;
    private Seat seat2;
    private List<Seat> seats;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        this.seat1 = new Seat(1L, 2, Boolean.TRUE, null);
        this.seat2 = new Seat(2L, 4, Boolean.TRUE, null);
        this.seats = Arrays.asList(seat1, seat2);
    }

    @Test
    public void getAllSeatTest() throws Exception {
        when(seatService.getAllSeat()).thenReturn(seats);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/seat")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].number", is(1)))
                .andExpect(jsonPath("$[1].number", is(2)));
    }

    @Test
    public void getSeatByIdSuccessTest() throws Exception {
        when(seatService.getByNumber(ArgumentMatchers.anyLong())).thenReturn(seat1);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/seat/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number", is(1)));
    }

    @Test
    public void getSeatByIdNotFoundTest() throws Exception {
        when(seatService.getByNumber(ArgumentMatchers.anyLong())).thenThrow(NoSuchElementException.class);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/seat/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAvailableSeatTest() throws Exception {
        String strDatetime = "2021-08-20 13:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime datetime = LocalDateTime.parse(strDatetime, formatter);
        when(reservationService.getAvailableSeats(datetime)).thenReturn((new HashSet<Seat>(seats)));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/seat/available?datetime="+strDatetime)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void addSeatTest() throws Exception {
        when(seatService.addFromRequest(ArgumentMatchers.any())).thenReturn(seat1);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/seat/upsert")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"number\": 1,\n" +
                        "    \"available\": \"true\",\n" +
                        "    \"capacity\": 4\n" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Seat 1 successfully added.")));
    }

    @Test
    public void deleteSeatTest() throws Exception {
        when(seatService.delete(ArgumentMatchers.any())).thenReturn(null);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/seat/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Seat 1 successfully deleted")));
    }
}
