package com.future.sushee.payload.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class OrderCreationRequest {

    @NotBlank
    private Integer amount;

    @NotBlank
    private Integer status;

    @NotBlank
    private Long reservationId;

    @NotBlank
    private Long menuId;
}
