package de.difuture.ekut.pht.train.controller.controller;

import de.difuture.ekut.pht.train.controller.repository.routeevent.RouteEvent;
import de.difuture.ekut.pht.train.controller.repository.routeevent.RouteEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<RouteEvent> getAll() {

        System.out.println("RouteEvent Repository has in Controller" + this.routeEventRepository.count());
        return this.routeEventRepository.findAll();
    }
}
