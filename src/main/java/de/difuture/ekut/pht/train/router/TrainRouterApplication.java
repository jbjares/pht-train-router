package de.difuture.ekut.pht.train.router;

import de.difuture.ekut.pht.config.Neo4jDenbiConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jDataAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.Import;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication(
    exclude = Neo4jDataAutoConfiguration.class,
    scanBasePackages = {
              "de.difuture.ekut.pht.config",
              "de.difuture.ekut.pht.train.router"})
@EnableNeo4jRepositories
@Import(Neo4jDenbiConfiguration.class)
@EnableScheduling
@EnableBinding(Processor.class)
@EnableFeignClients
public class TrainRouterApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainRouterApplication.class, args);
	}
}
