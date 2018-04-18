package de.difuture.ekut.pht.train.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;


@SpringBootApplication
@EnableBinding(Sink.class)
@EnableNeo4jRepositories("de.difuture.ekut.pht.train.controller.repository")
public class TrainControllerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainControllerApplication.class, args);
	}
}
