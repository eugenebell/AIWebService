package com.eugene.aiwebtester.ai;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AiWebServiceApplication {
	private static final Logger LOG = LogManager.getLogger(AiWebServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AiWebServiceApplication.class, args);
	}

}
