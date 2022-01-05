package com.asecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AsecurityApplication {// implements CommandLineRunner {

	public static void main(String[] args) {
//		System.out.println(new BCryptPasswordEncoder().encode("123"));

		SpringApplication.run(AsecurityApplication.class, args);
	}

//	@Autowired
//	private JavaMailSender sender;
//
//	@Override
//	public void run(String... args) throws Exception {
//		SimpleMailMessage mail = new SimpleMailMessage();
//		mail.setFrom("emailchecktask@gmail.com");
//		mail.setTo("msergiost@hotmail.com");
//		mail.setText("tested,.........");
//		mail.setSubject("teste 3");
//
//		sender.send(mail);
//
//	}

}
