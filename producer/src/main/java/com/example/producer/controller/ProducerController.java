package com.example.producer.controller;

import com.example.producer.service.CarService;
import com.example.producer.util.DealerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/producer")
public class ProducerController {
    @Autowired
    private CarService carService;

    @PostMapping("/send")
    public ResponseEntity<String> sendCarsByDealerToKafka(@RequestParam String dealerName) {
        try {
            carService.sendCarsByDealerNameToKafka(dealerName);
            return ResponseEntity.ok("Data sent to Kafka successfully for dealer: " + dealerName);
        } catch (DealerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send data to Kafka: " + e.getMessage());
        }
    }


}
