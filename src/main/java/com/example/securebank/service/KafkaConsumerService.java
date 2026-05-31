package com.example.securebank.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.securebank.event.TransactionEvent;

@Service
public class KafkaConsumerService {

    private static final Logger logger =
            LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(
            topics = "transaction-events",
            groupId = "securebank-group"
    )
    public void consumeTransactionEvent(TransactionEvent event) {

        logger.info("Kafka event received");

        logger.info("Account Number: {}", event.getAccountNumber());

        logger.info("Transaction Type: {}", event.getTransactionType());

        logger.info("Amount: {}", event.getAmount());

        logger.info("Description: {}", event.getDescription());

        logger.info("Created At: {}", event.getCreatedAt());
    }
}