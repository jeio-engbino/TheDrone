package org.hitachi.drone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TheDroneApplication {

	public static void main(String[] args) {
		SpringApplication.run(TheDroneApplication.class, args);
	}

}
