package de.difuture.ekut.pht.train.router.controller;

import de.difuture.ekut.pht.lib.core.messages.TrainAvailable;
import de.difuture.ekut.pht.train.router.repository.routeevent.RouteEvent;
import de.difuture.ekut.pht.train.router.repository.routeevent.RouteEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * Provides the handlers for the /routeevent route
 *
 * @author Lukas Zimmermann
 */
@RestController
@RequestMapping("/routeevent")
public class RouteEventController {

    private final RouteEventRepository routeEventRepository;

    @Autowired
    public RouteEventController(RouteEventRepository routeEventRepository) {

        this.routeEventRepository = routeEventRepository;
    }

    @StreamListener(target=Processor.INPUT)
    @Transactional(
            transactionManager = "jpaTransactionManager",
            propagation = Propagation.REQUIRES_NEW)
    public void sink(TrainAvailable trainAvailable) {

        // Save the trainAvailable message as RouteEvent
        this.routeEventRepository.saveAndFlush(
                new RouteEvent(
                        trainAvailable.getTrainID(),
                        trainAvailable.getTrainTag())
        );
    }

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<RouteEvent> getAll() {

        return this.routeEventRepository.findAll();
    }
}
