package com.future.sushee.payload.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class SeatCreationRequest {

    @NotBlank
    private Long number;

    @NotBlank
    private Boolean available;

    @NotBlank
    private Integer capacity;
}
