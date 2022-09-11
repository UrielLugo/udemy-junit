package com.uriellugo.udemyjunit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class UdemyJUnitApplication {

	public static void main(String[] args) {
		SpringApplication.run(UdemyJUnitApplication.class, args);
	}
}
