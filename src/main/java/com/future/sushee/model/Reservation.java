package com.future.sushee.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1,max = 4)
    @Column(name = "number_of_person", nullable = false)
    private Integer numberOfPerson;

    @NotNull
    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @NotNull
    @Column(name = "starting_date_time", nullable = false)
    private LocalDateTime startingDateTime;

    @NotNull
    @Column(name = "status", nullable = false)
    // -1 = rejected/deactivated, 0 = active(waiting until mealtime),
    // 1 = active(on meal time), 2 = done(post meal time)
    // 3 = expired(post meal time)
    private Integer status;

    @OneToMany(mappedBy = "reservation",fetch=FetchType.LAZY)
    @JsonIgnore
    private List<Order> orders;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "uuid")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "seat_number", referencedColumnName = "number", nullable = false)
    @JsonIgnore
    private Seat seat;
}
