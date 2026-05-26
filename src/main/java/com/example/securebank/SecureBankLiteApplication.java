package com.example.securebank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@EnableCaching
@SpringBootApplication
public class SecureBankLiteApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecureBankLiteApplication.class, args);
	}

}
