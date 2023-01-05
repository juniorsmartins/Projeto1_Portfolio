package io.micro_dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient // Spring Cloud Netflix - informa à aplicação da necessidade de se registrar num server Eureka
public class MicroDashboardApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroDashboardApplication.class, args);
	}
}
