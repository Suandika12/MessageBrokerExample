package com.example.producer.service;


import com.example.producer.model.dto.CarDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProducerService {
    @Value("${spring.kafka.topics.local}")
    String topic;

    @Value("${useKafkaLocalProducer}")
    String useKafkaLocalProducer;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) {
        this.kafkaTemplate.send(topic, message);
    }

    public void sendData(List<CarDto> listCollateral) {
        Gson gson = new GsonBuilder().serializeNulls().create();

        if (Boolean.parseBoolean(useKafkaLocalProducer)){
            this.kafkaTemplate.send(topic, gson.toJson(listCollateral));
        }
    }
}
