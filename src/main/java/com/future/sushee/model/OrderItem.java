package com.future.sushee.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private MenuModel menu;
    private Integer amount;
    private Integer status;
}
