package cz.mbucek.PurkiadaGateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;



@SpringBootApplication
@EnableEurekaClient
public class PurkiadaGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(PurkiadaGatewayApplication.class, args);
	}

}
