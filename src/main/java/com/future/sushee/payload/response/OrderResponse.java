package com.future.sushee.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long id;
    private Integer amount;
    private Integer status;
    private Long menu;
    private Long reservation;
}
