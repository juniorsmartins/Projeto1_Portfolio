package io.teste_openfeign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient // Spring Cloud Netflix - informa à aplicação da necessidade de se registrar num server Eureka
@EnableFeignClients // Spring Cloud OpenFeign - permite uso de comunicação síncrona com microservices
public class TesteOpenfeignApplication {

	public static void main(String[] args) {
		SpringApplication.run(TesteOpenfeignApplication.class, args);
	}

}
