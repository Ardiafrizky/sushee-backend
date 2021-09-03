package com.future.sushee.payload.response;

import com.future.sushee.model.Menu;
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
    private Menu menu;
    private Long reservation;
}
