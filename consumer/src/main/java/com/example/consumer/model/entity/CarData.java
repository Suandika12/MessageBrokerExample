package com.example.consumer.model.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "car_data")
public class CarData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CAR_ID")
    private String carId;

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

    @Column(name = "CREATED_AT")
    private Date create_At;
}
