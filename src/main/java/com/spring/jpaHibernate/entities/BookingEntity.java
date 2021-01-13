package com.spring.jpaHibernate.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@MappedSuperclass
@Data
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 200)
    private String name;

    @Column(name = "description", columnDefinition = "LONGTEXT" )
    private String description;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "price")
    private Double price;

    @Column(name = "people_amount")
    private Byte peopleAmount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private Date deletedAt;

    @ManyToMany
    private List<Users> users;

    @PrePersist
    public void prePersist(){
        this.createdAt = new Date();
    }

    @PreUpdate
    public void preUpdate(){
        this.updatedAt = new Date();
    }

    @PreRemove
    public void preRemove(){
        this.deletedAt = new Date();
    }

}
