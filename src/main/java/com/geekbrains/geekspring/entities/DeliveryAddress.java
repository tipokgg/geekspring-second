package com.geekbrains.geekspring.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "delivery_addresses")
@Data
public class DeliveryAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "address")
    private String address;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
