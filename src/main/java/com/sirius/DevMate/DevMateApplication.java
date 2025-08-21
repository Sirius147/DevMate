package com.sirius.DevMate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DevMateApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevMateApplication.class, args);
	}

}
