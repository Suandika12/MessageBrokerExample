package com.example.consumer.repository;

import com.example.consumer.model.entity.CarData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarDataRepository extends JpaRepository<CarData, Long> {
    Optional<CarData> findByCarId(String carId);
}

