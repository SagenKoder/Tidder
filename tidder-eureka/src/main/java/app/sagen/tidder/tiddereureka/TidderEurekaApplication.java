package app.sagen.tidder.tiddereureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class TidderEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TidderEurekaApplication.class, args);
    }

}
