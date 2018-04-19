package de.difuture.ekut.pht.train.controller.controller;

import de.difuture.ekut.pht.lib.core.messages.TrainAvailable;
import de.difuture.ekut.pht.train.controller.repository.RouteEvent;
import de.difuture.ekut.pht.train.controller.repository.RouteEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


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
        this.routeEventRepository.save(
                new RouteEvent(
                        trainAvailable.getTag(),
                        trainAvailable.getTrainID()));
    }

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<RouteEvent> getAll() {

        return this.routeEventRepository.findAll();
    }
}
