package de.difuture.ekut.pht.train.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(exclude = Neo4jDataAutoConfiguration.class)
@EnableScheduling
public class TrainControllerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainControllerApplication.class, args);
	}
}
