package de.difuture.ekut.pht.train.controller.controller;

import de.difuture.ekut.pht.lib.core.messages.TrainAvailable;
import de.difuture.ekut.pht.train.controller.repository.RouteEventRepository;
import de.difuture.ekut.pht.train.controller.repository.Station;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;


/**
 * Provides the handlers for the /routeevent route
 *
 * @author Lukas Zimmermann
 */
@RestController
@EnableBinding(Sink.class)
@RequestMapping("/routeevent")
public class RouteEventController {

    private final RouteEventRepository routeEventRepository;

    @Autowired
    public RouteEventController(RouteEventRepository routeEventRepository) {

        this.routeEventRepository = routeEventRepository;
    }

    @StreamListener(target=Sink.INPUT)
    public void sink(TrainAvailable trainAvailable) {

        System.out.println(trainAvailable);
        // Save the trainAvailable message as RouteEvent
        //final RouteEvent event = new RouteEvent(trainAvailable.getTag(), trainAvailable.getTrainID());
        System.out.println("SAVED as " + this.routeEventRepository.save(new Station(null, URI.create("localhost"))).getId());

        System.out.println("RouteEvent Repository has in Sink" + this.routeEventRepository.count());
    }

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Station> getAll() {

        System.out.println("RouteEvent Repository has in Controller" + this.routeEventRepository.count());
        return this.routeEventRepository.findAll();
    }
}
