package com.future.sushee.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "name",nullable = false, unique = true)
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

    @NotNull
    @Column(name = "status", nullable = false)
    // 0 = deactivated, 1 = activated
    private Integer status;

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Order> orders;
}
