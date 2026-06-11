package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class UserServiceAppExecutable {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceAppExecutable.class, args);
    }
}
