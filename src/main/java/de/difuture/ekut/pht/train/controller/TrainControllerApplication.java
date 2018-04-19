package de.difuture.ekut.pht.train.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration;


@SpringBootApplication(exclude = Neo4jDataAutoConfiguration.class)
public class TrainControllerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainControllerApplication.class, args);
	}
}
