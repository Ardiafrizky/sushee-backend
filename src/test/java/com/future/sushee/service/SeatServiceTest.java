package com.future.sushee.service;

import com.future.sushee.model.Seat;
import com.future.sushee.payload.request.SeatCreationRequest;
import com.future.sushee.repository.SeatRepository;
import com.future.sushee.service.implementations.SeatServiceImpl;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Tag("service")
public class SeatServiceTest {

    @InjectMocks
    @Spy
    private SeatServiceImpl seatService;

    @Mock
    private SeatRepository seatRepository;

    @Captor
    private ArgumentCaptor<Seat> seatArgumentCaptor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach()
    public void afterEach() {
        reset(seatRepository, seatService);
    }

    @Test
    public void getAllSeatTestSuccess() {
        Seat seat1 = new Seat();
        Seat seat2 = new Seat();
        List<Seat> seats = Arrays.asList(seat1, seat2);
        when(seatRepository.findAll()).thenReturn(seats);

        List<Seat> result = seatService.getAllSeat();
        verify(seatRepository).findAll();

        assertEquals(result.size(), 2);
        assertEquals(result.get(0), seat1);
        assertEquals(result.get(1), seat2);
    }

    @Test
    public void addSeatTestSuccess() {
        Seat seat = new Seat();
        when(seatRepository.save(seat)).thenReturn(seat);

        Seat result = seatService.add(seat);
        verify(seatRepository).save(seatArgumentCaptor.capture());

        assertEquals(result, seat);
        assertEquals(seat, seatArgumentCaptor.getValue());
    }

    @Test
    public void getByNumberTestSuccess() {
        Seat seat = new Seat();
        seat.setNumber(1L);
        when(seatRepository.findByNumber(ArgumentMatchers.anyLong())).thenReturn(Optional.of(seat));

        Seat result = seatService.getByNumber(1L);
        verify(seatRepository).findByNumber(1L);
        assertEquals(seat, result);
    }

    @Test
    public void deleteTestSuccess() {
        Seat seat = new Seat();
        Seat result = seatService.delete(seat);
        verify(seatRepository).delete(seatArgumentCaptor.capture());
        assertEquals(seat, result);
        assertEquals(seat, seatArgumentCaptor.getValue());
    }

    @Test
    public void addFromRequestTestSuccess() {
        Seat seat = new Seat();
        SeatCreationRequest seatCreationRequest = new SeatCreationRequest();
        seatCreationRequest.setAvailable(Boolean.FALSE);
        seatCreationRequest.setCapacity(4);
        seatCreationRequest.setNumber(10L);

        doReturn(seat).when(seatService).add(seat);
        Seat result = seatService.addFromRequest(seatCreationRequest);

        verify(seatService).add(seatArgumentCaptor.capture());
        assertEquals(seatArgumentCaptor.getAllValues().get(0).getNumber(), 10L);
    }
}
