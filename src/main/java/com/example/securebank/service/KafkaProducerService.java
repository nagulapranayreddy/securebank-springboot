package com.example.securebank.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.securebank.event.TransactionEvent;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, TransactionEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTransactionEvent(TransactionEvent event) {

        System.out.println("Sending event to Kafka:");
        System.out.println("Type: " + event.getTransactionType());
        System.out.println("Account: " + event.getAccountNumber());
        System.out.println("Amount: " + event.getAmount());

        kafkaTemplate.send("transaction-events", event);
    }
}