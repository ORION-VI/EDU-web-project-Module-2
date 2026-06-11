package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
public class ConfigServerAppExecutable {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerAppExecutable.class);
    }
}
