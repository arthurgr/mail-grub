package com.example.mailgrub;

import org.springframework.boot.SpringApplication;

public class TestMailgrubApplication {

	public static void main(String[] args) {
		SpringApplication.from(MailgrubApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
