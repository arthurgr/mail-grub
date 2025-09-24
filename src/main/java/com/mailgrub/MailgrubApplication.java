package com.mailgrub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MailgrubApplication {

  public static void main(String[] args) {
    SpringApplication.run(MailgrubApplication.class, args);
  }
}
