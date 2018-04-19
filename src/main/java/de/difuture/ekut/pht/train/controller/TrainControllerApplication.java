package de.difuture.ekut.pht.train.controller;

import de.difuture.ekut.pht.lib.core.messages.TrainAvailable;
import de.difuture.ekut.pht.train.controller.repository.routeevent.RouteEvent;
import de.difuture.ekut.pht.train.controller.repository.routeevent.RouteEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@SpringBootApplication
@EnableNeo4jRepositories("de.difuture.ekut.pht.train.controller.repository.traindestination")
@EnableBinding(Sink.class)
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
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sink(TrainAvailable trainAvailable) {

        System.out.println(trainAvailable);
        // Save the trainAvailable message as RouteEvent
        final RouteEvent event = new RouteEvent();
        System.out.println("SAVED as " + this.routeEventRepository.saveAndFlush(event).getId());


        System.out.println("RouteEvent Repository has in Sink" + this.routeEventRepository.count());
    }
}
