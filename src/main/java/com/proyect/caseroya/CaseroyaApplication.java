package com.proyect.caseroya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class CaseroyaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CaseroyaApplication.class, args);
	}

}


