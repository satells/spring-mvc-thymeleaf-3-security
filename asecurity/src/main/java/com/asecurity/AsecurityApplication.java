package com.asecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AsecurityApplication {

	public static void main(String[] args) {
//		System.out.println(new BCryptPasswordEncoder().encode("123"));

		SpringApplication.run(AsecurityApplication.class, args);
	}

}
