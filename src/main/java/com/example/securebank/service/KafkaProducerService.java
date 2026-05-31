package com.example.securebank.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.securebank.event.TransactionEvent;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;

    private static final Logger logger =
            LoggerFactory.getLogger(KafkaProducerService.class);

    public KafkaProducerService(KafkaTemplate<String, TransactionEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendTransactionEvent(TransactionEvent event) {

        logger.info("Sending event to Kafka");
        logger.info("Transaction Type: {}", event.getTransactionType());
        logger.info("Account Number: {}", event.getAccountNumber());
        logger.info("Amount: {}", event.getAmount());

        kafkaTemplate.send("transaction-events", event);
    }
}