package com.bv.kafkaui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KafkaUiStarter {

	public static void main(String[] args) {
		SpringApplication.run(KafkaUiStarter.class, args);
	}
}