package com.spring.jpaHibernate.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @ManyToMany(fetch = FetchType.EAGER)
    private List <Roles> roles;

    @ManyToMany(mappedBy = "users")
    private List<Hotel> hotels;

    @Column(name = "ava")
    private String ava;

    @Column(name = "ava_hash")
    private String avaHash;

}
