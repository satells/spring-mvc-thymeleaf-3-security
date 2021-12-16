package com.asecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class AsecurityApplication {

	public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder().encode("root"));

		SpringApplication.run(AsecurityApplication.class, args);
	}

}
