package com.eduAcademy.management_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManagementSystemApplication.class, args);
	}

}
