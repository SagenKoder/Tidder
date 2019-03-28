package app.sagen.tiddertest1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class TidderTest1Application {

    public static void main(String[] args) {
        SpringApplication.run(TidderTest1Application.class, args);
    }

}
