package com.todaysfortune;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PlaytodaysfortuneApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlaytodaysfortuneApplication.class, args);
	}

}
