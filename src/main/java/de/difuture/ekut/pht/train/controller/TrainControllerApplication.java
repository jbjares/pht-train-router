package de.difuture.ekut.pht.train.controller;

import de.difuture.ekut.pht.train.controller.message.TrainUpdateStreams;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;


@SpringBootApplication
@EnableDiscoveryClient
@EnableBinding(TrainUpdateStreams.class)
public class TrainControllerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainControllerApplication.class, args);
	}
}
