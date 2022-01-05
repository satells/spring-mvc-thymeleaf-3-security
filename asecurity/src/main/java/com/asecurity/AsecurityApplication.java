package com.asecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.javamail.JavaMailSender;

import com.asecurity.service.EmailService;

@SpringBootApplication
public class AsecurityApplication implements CommandLineRunner {

	public static void main(String[] args) {
//		System.out.println(new BCryptPasswordEncoder().encode("123"));

		SpringApplication.run(AsecurityApplication.class, args);
	}

	@Autowired
	private JavaMailSender sender;

	@Autowired
	private EmailService emailService;

	@Override
	public void run(String... args) throws Exception {
		emailService.enviarPedidoDeConformacaoDeCadastro("msergiost@hotmail.com", "jrlçewqueiwaopnj789");

//		SimpleMailMessage mail = new SimpleMailMessage();
//		mail.setFrom("emailchecktask@gmail.com");
//		mail.setTo("msergiost@hotmail.com");
//		mail.setText("tested,.........");
//		mail.setSubject("teste 3");
//
//		sender.send(mail);

	}

}
