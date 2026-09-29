package com.example.producer.service;

import com.example.producer.model.dto.CarDto;
import com.example.producer.model.entity.Car;
import com.example.producer.repository.CarRepository;
import com.example.producer.util.DealerNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.producer.util.ErrorMessage.DEALER_NOT_FOUND;

@Service
public class CarService {
    private static final Logger logger = LoggerFactory.getLogger(CarService.class);

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ProducerService producerService;

    private static final int BATCH_SIZE = 10;

    private CarDto mapToDto(Car car) {
        CarDto dto = new CarDto();
        dto.setId(car.getId());
        dto.setBrand(car.getBrand());
        dto.setModel(car.getModel());
        dto.setYear(car.getYear());
        dto.setColor(car.getColor());
        dto.setPrice(car.getPrice());
        dto.setJenis(car.getJenis());
        if (car.getDealer() != null) {
            dto.setDealer(car.getDealer().getName());
        }
        return dto;
    }

    public List<CarDto> findCarsByDealerName(String dealerName) {
        return carRepository.findByDealer_Name(dealerName)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public void sendCarsByDealerNameToKafka(String dealerName) {
        List<CarDto> cars = findCarsByDealerName(dealerName);

        if (cars.isEmpty()) {
            throw new DealerNotFoundException(String.format(DEALER_NOT_FOUND, dealerName));
        }

        logger.info("Total data ditemukan untuk dealer '{}': {} data", dealerName, cars.size());

        for (int i = 0; i < cars.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, cars.size());
            List<CarDto> batch = cars.subList(i, end);

            logger.info("Mengirim batch ke Kafka. Dealer: '{}', Batch ke-{} ({} - {}) dari total {} data",
                    dealerName, (i / BATCH_SIZE + 1), i + 1, end, cars.size());

            producerService.sendData(batch);
        }

        logger.info("Pengiriman seluruh data untuk dealer '{}' selesai.", dealerName);
    }
}
