package com.exploration.rtb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AuctionEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuctionEngineApplication.class, args);
	}

}
