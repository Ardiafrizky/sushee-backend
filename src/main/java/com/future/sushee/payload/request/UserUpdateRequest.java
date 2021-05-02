package com.future.sushee.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;


@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest extends UserCreationRequest {

    @NotBlank
    @Size(min = 8, max = 40)
    private String oldPassword;
}
