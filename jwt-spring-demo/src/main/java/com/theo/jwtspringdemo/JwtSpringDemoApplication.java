package com.theo.jwtspringdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages= {"com.theo.jwtspringdemo"})
public class JwtSpringDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(JwtSpringDemoApplication.class, args);
	}

}
