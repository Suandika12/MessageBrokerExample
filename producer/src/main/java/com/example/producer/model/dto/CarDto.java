package com.example.producer.model.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigInteger;

@Data
public class CarDto {
    private BigInteger id;
    private String brand;
    private String model;
    private Integer year;
    private String color;
    private String price;
    private String dealer;
    private String jenis;
}
