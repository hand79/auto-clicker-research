package com.max.jna;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@ComponentScan(basePackages = { "com.max.jna" })  
@SpringBootApplication
public class MaxDemoJnaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MaxDemoJnaApplication.class, args);
	}
	
	@Bean("objectMapper")
    public ObjectMapper myMapper() {
        return new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

}

