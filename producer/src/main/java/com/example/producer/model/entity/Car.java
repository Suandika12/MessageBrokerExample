package com.example.producer.model.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigInteger;

@Data
@Entity
@Table(name = "cars")
public class Car {

    @Id
    @Column(name = "ID")
    private BigInteger id;

    @Column(name = "BRAND")
    private String brand;

    @Column(name = "MODEL")
    private String model;

    @Column(name = "YEAR")
    private Integer year;

    @Column(name = "COLOR")
    private String color;

    @Column(name = "PRICE")
    private String price;

    @Column(name = "JENIS")
    private String jenis;

    @ManyToOne
    @JoinColumn(name = "DEALER_ID")
    private Dealer dealer;



}
