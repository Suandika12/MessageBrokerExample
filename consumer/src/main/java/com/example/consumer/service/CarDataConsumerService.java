package com.example.consumer.service;

import com.example.consumer.model.dto.CarMessageDto;
import com.example.consumer.model.entity.CarData;
import com.example.consumer.repository.CarDataRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CarDataConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(CarDataConsumerService.class);

    @Autowired
    private CarDataRepository carDataRepository;

    @Value("${useKafkaDealerConsumer}")
    private String useKafkaDealerConsumer;

    @KafkaListener(topics = "${spring.kafka.topics.local}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        if (!Boolean.parseBoolean(useKafkaDealerConsumer)) {
            logger.info("Kafka consumer is disabled via useKafkaDealerConsumer. Skipping message.");
            acknowledgment.acknowledge();
            return;
        }

        String message = record.value();
        long offset = record.offset();
        int partition = record.partition();
        String topic = record.topic();

        logger.info("Received Kafka message: [topic: {}, partition: {}, offset: {}, value: {}]",
                topic, partition, offset, message);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<CarMessageDto>>() {}.getType();

        List<CarMessageDto> carMessageList;
        try {
            carMessageList = gson.fromJson(message, listType);
        } catch (Exception e) {
            logger.error("Failed to parse Kafka message: {}", e.getMessage());
            acknowledgment.acknowledge();
            return;
        }

        if (carMessageList == null || carMessageList.isEmpty()) {
            logger.warn("No data provided in Kafka message.");
            acknowledgment.acknowledge();
            return;
        }

        for (CarMessageDto dto : carMessageList) {
            if ("Dealer Toyota".equalsIgnoreCase(dto.getDealer())) {
                try {
                    Optional<CarData> existingData = carDataRepository.findByCarId(String.valueOf(dto.getId()));
                    CarData carData;
                    if (existingData.isPresent()) {
                        // Jika ada, update datanya
                        carData = existingData.get();
                        logger.info("Updating existing CarData with CAR_ID={}", carData.getCarId());
                    } else {
                        // Jika belum ada, buat baru
                        carData = new CarData();
                        carData.setCarId(String.valueOf(dto.getId()));
                        logger.info("Creating new CarData with CAR_ID={}", carData.getCarId());
                    }

                    carData.setModel(dto.getModel());
                    carData.setYear(dto.getYear());
                    carData.setColor(dto.getColor());
                    carData.setPrice(dto.getPrice());
                    carData.setJenis(dto.getJenis());
                    carData.setCreate_At(new Date());

                    carDataRepository.save(carData);

                } catch (Exception e) {
                    logger.error("Error saving CarData with CAR_ID={}: {}", dto.getId(), e.getMessage(), e);
                }
            }
        }

        acknowledgment.acknowledge(); // Commit offset hanya setelah semua proses selesai
        logger.info("Finished processing Kafka message. Offset committed.");
    }
}