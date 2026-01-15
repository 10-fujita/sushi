package com.example.springlesson;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringlessonApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringlessonApplication.class, args);
	}

}
