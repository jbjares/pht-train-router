package de.difuture.ekut.pht.train.router;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(exclude = Neo4jDataAutoConfiguration.class)
@EnableScheduling
@EnableBinding(Processor.class)
@EnableFeignClients
public class TrainRouterApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainRouterApplication.class, args);
	}
}
