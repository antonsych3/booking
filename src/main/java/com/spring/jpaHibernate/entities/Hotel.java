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
@Table(name = "hotels")
public class Hotel extends BookingEntity {


    @Column(name = "stars")
    private byte stars;

    @Column(name = "rooms_amount")
    private Byte roomsAmount;

}
