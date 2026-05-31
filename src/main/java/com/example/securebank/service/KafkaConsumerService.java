package com.example.securebank.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.securebank.event.TransactionEvent;

@Service
public class KafkaConsumerService {

    @KafkaListener(
            topics = "transaction-events",
            groupId = "securebank-group"
    )
    public void consumeTransactionEvent(TransactionEvent event) {

        System.out.println("Kafka event received:");
        System.out.println("Account Number: " + event.getAccountNumber());
        System.out.println("Transaction Type: " + event.getTransactionType());
        System.out.println("Amount: " + event.getAmount());
        System.out.println("Description: " + event.getDescription());
        System.out.println("Created At: " + event.getCreatedAt());
    }
}



//package com.example.securebank.service;
//
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//
//@Service
//public class KafkaConsumerService {
//
//    @KafkaListener(
//            topics = "transaction-events",
//            groupId = "securebank-group"
//    )
//    public void consumeTransactionEvent(String message) {
//
//        System.out.println("Kafka RAW Message Received:");
//        System.out.println(message);
//    }
//}