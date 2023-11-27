package com.test.personservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class PersonServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(PersonServiceApplication.class, args);
  }

}
