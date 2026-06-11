package example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class ServiceDiscoveryAppExecutable {

    public static void main(String[] args) {
        SpringApplication.run(ServiceDiscoveryAppExecutable.class, args);
    }
}
