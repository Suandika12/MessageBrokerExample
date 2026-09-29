package com.example.consumer.model.dto;

import lombok.Data;

@Data
public class CarMessageDto {
    private Long id;
    private String brand;
    private String model;
    private Integer year;
    private String color;
    private String price;
    private String dealer;
    private String jenis;
}
