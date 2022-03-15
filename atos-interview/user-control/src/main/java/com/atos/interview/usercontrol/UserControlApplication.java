package com.atos.interview.usercontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class UserControlApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserControlApplication.class, args);
		log.info("Application started");
	}

}
