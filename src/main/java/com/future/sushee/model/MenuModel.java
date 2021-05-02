package com.future.sushee.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="menu")
public class MenuModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "name",nullable = false)
    private String name;

    @NotNull
    @Size(max = 200)
    @Column(name = "description",nullable = false)
    private String description;

    @NotNull
    @Size(max = 20)
    @Column(name = "unit",nullable = false)
    private String unit;

    @NotNull
    @Size(max = 200)
    @Column(name = "image",nullable = true)
    private String imageUrl;
}
