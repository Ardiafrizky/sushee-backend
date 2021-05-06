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
@Table(name="seat")
public class Seat {

    @Id @NotNull
    @Column(name = "number", nullable = false)
    private Long number;

    @NotNull
    @Size(max = 4)
    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @NotNull
    @Size(max = 4)
    @Column(name = "available", nullable = false)
    private Boolean available;

    @JsonIgnore
    @OneToMany(mappedBy = "seat",fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Reservation> reservations;
}
