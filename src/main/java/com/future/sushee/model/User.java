package com.future.sushee.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid",strategy = "uuid")
    private String uuid;

    @NotNull
    @Size(max = 50)
    @Column(name = "email",nullable = false)
    private String email;

    @NotNull
    @Size(max = 20)
    @Column(name = "username",nullable = false)
    private String username;

    @NotNull
    @Size(max = 100)
    @Column(name = "fullName",nullable = false)
    private String fullName;

    @NotNull @Lob
    @Size(max = 200)
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "password",nullable = false)
    @JsonIgnore
    private String password;

    @NotNull
    @Size(max = 200)
    @Column(name = "image",nullable = true)
    private String imageUrl;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable( name = "user_roles",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user",fetch=FetchType.LAZY)
    @JsonIgnore
    private Set<Reservation> reservations;

    public User(
            String username, String fullname, String email,
            String password, String imageUrl
    ){
        this.username = username;
        this.fullName = fullname;
        this.imageUrl = imageUrl;
        this.password = password;
        this.email = email;
    }
}
