package de.difuture.ekut.pht.train.router.service;

import de.difuture.ekut.pht.lib.core.traintag.InvalidTrainTagException;
import de.difuture.ekut.pht.lib.core.traintag.TrainTag;
import de.difuture.ekut.pht.lib.core.traintag.TrainTagLiteral;
import de.difuture.ekut.pht.train.router.repository.routeevent.RouteEvent;
import de.difuture.ekut.pht.train.router.repository.routeevent.RouteEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;


/**
 * Component scans events happening on the route and
 *
 */
@Component
public class RouteEventProcessor {

    // Needed for fetching unprocessed route events
    private final RouteEventRepository routeEventRepository;

    // Needed for route planning of START trains
    private final RoutePlanner routePlanner;

    @Autowired
    public RouteEventProcessor(
            RouteEventRepository routeEventRepository,
            RoutePlanner routePlanner) {

        this.routeEventRepository = routeEventRepository;
        this.routePlanner = routePlanner;
    }

    // Processes one single route event that has not been visited before
    @Scheduled(fixedDelay = 1000)
    private void processEvent() {

        final Optional<RouteEvent> existingRouteEvent
                = this.routeEventRepository.getFirstByProcessedIsFalse();

        // Only do something if there is an unprocessed route event
        if (existingRouteEvent.isPresent()) {

            final RouteEvent routeEvent = existingRouteEvent.get();

            try {
                final TrainTag trainTag = TrainTag.of(routeEvent.getTag());

                // If the route Event is marked with a start trainTag, we need to generate a new route
                // for this train
                if (trainTag == TrainTagLiteral.START) {

                    this.routePlanner.plan(routeEvent.getTrainID());
                }

            // This event is invalid, since the trainTag of the train is not allowe
            } catch (InvalidTrainTagException e) {

                this.routeEventRepository.delete(routeEvent);
                // TODO Logging
            }

            routeEvent.setProcessed(true);
            routeEvent.setProcessedInstant(Instant.now());
            this.routeEventRepository.saveAndFlush(routeEvent);
        }
    }

    private void sendTrainReady() {


    }
}
