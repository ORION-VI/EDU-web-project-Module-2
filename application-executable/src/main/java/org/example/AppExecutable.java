package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication(scanBasePackages = {"org.example"})
@EnableKafka
public class AppExecutable {

    public static void main(String[] args) {
        SpringApplication.run(AppExecutable.class, args);
    }
}
