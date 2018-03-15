package de.difuture.ekut.pht.trainrouter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class TrainrouterApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainrouterApplication.class, args);
	}
}
