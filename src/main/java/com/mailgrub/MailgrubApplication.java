package com.mailgrub;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableCaching
public class MailgrubApplication {

  public static void main(String[] args) {
    SpringApplication.run(MailgrubApplication.class, args);
  }
}
