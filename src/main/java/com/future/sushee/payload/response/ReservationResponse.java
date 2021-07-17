package com.future.sushee.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponse {
    private Long id;
    private Integer numberOfPerson;
    private Integer totalPrice;
    private LocalDateTime startingDateTime;
    private Integer status;
    private String user;
    private Long seat;
}
