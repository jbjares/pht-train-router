package de.difuture.ekut.pht.train.controller;

import de.difuture.ekut.pht.lib.core.messages.TrainAvailable;
import de.difuture.ekut.pht.train.controller.repository.RouteEvent;
import de.difuture.ekut.pht.train.controller.repository.RouteEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;


@SpringBootApplication
@EnableBinding(Sink.class)
@EnableNeo4jRepositories("de.difuture.ekut.pht.train.controller.repository")
public class TrainControllerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainControllerApplication.class, args);
	}

	private final RouteEventRepository routeEventRepository;

	@Autowired
	public TrainControllerApplication(RouteEventRepository routeEventRepository) {

		this.routeEventRepository = routeEventRepository;
	}

	@StreamListener(target=Sink.INPUT)
	public void sink(TrainAvailable trainAvailable) {

		System.out.println(trainAvailable);
		// Save the trainAvailable message as RouteEvent
		this.routeEventRepository.save(
				new RouteEvent(
						trainAvailable.getTag(),
						trainAvailable.getTrainID()));
	}
}
