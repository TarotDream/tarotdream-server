package com.sunkyuj.tarotdream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:/application.properties")
public class TarotdreamApplication {

	public static void main(String[] args) {
		SpringApplication.run(TarotdreamApplication.class, args);
	}

}
