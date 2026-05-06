package com.practice.flightbooking;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableRabbit
@EnableCaching
public class FlightbookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlightbookingApplication.class, args);
	}

}
