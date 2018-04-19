package de.difuture.ekut.pht.train.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;


@SpringBootApplication
@EnableNeo4jRepositories("de.difuture.ekut.pht.train.controller.repository.traindestination")
@EnableJpaRepositories(basePackages = {
        "de.difuture.ekut.pht.train.controller.repository.routeevent",
        "de.difuture.ekut.pht.train.controller.repository.station"})
public class TrainControllerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainControllerApplication.class, args);
	}
}
