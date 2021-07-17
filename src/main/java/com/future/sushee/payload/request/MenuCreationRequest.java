package com.future.sushee.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuCreationRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotBlank
    private String unit;

    @NotBlank
    private String imageUrl;

    @NotNull
    private Integer status;

}
