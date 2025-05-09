package org.example.racescheduleservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class RaceScheduleServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(RaceScheduleServiceApplication.class, args);
	}
}