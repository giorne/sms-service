package com.gs.smsservice;

import com.gs.smsservice.gateways.memory.UserMemory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
public class SmsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmsServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(){
		return args -> {
			UserMemory.newUser(
					UserMemory.User.builder()
						.username("Giorne")
						.deviceId("12345")
						.balance(BigDecimal.valueOf(1000))
						.build()
			);

			UserMemory.newUser(
					UserMemory.User.builder()
						.username("Flitz")
						.deviceId("54321")
						.balance(BigDecimal.valueOf(500))
						.build()
			);

			UserMemory.newUser(
					UserMemory.User.builder()
						.username("Paul")
						.deviceId("159753")
						.balance(BigDecimal.valueOf(200))
						.build()
			);
		};
	}

}
