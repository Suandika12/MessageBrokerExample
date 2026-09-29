package com.example.producer.repository;

import com.example.producer.model.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;

public interface CarRepository extends JpaRepository<Car, BigInteger> {
    List<Car> findByDealer_Name(String dealerName);
}
