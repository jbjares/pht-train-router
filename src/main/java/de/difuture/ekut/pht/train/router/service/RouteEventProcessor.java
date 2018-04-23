package de.difuture.ekut.pht.train.router.service;

import de.difuture.ekut.pht.lib.core.messages.TrainVisit;
import de.difuture.ekut.pht.lib.core.traintag.InvalidTrainTagException;
import de.difuture.ekut.pht.lib.core.traintag.TrainTag;
import de.difuture.ekut.pht.train.router.client.StationClient;
import de.difuture.ekut.pht.train.router.repository.routeevent.RouteEvent;
import de.difuture.ekut.pht.train.router.repository.routeevent.RouteEventRepository;
import de.difuture.ekut.pht.train.router.repository.traindestination.TrainDestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.Instant;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Component scans events happening on the trainrouteassignment and
 *
 */
@Component
public class RouteEventProcessor {

    // Needed for fetching unprocessed trainrouteassignment events
    private final RouteEventRepository routeEventRepository;

    private final TrainDestinationRepository trainDestinationRepository;

    // Needed for trainrouteassignment planning of START trains
    private final StationClient stationClient;

    private final Processor processor;

    private final Queue<Long> pendingTrainVisits;


    @Autowired
    public RouteEventProcessor(
            RouteEventRepository routeEventRepository,
            TrainDestinationRepository trainDestinationRepository,
            StationClient stationClient,
            Processor processor) {

        this.routeEventRepository = routeEventRepository;
        this.trainDestinationRepository = trainDestinationRepository;
        this.stationClient = stationClient;
        this.processor = processor;

        this.pendingTrainVisits = new LinkedBlockingQueue<>();
    }

    // Processes one single trainrouteassignment event that has not been visited before
    @Scheduled(fixedDelay = 1000)
    private void processEvent() {

        final Optional<RouteEvent> existingRouteEvent
                = this.routeEventRepository.getFirstByProcessedIsFalse();

        // Only do something if there is an unprocessed trainrouteassignment event
        if (existingRouteEvent.isPresent()) {

            final RouteEvent routeEvent = existingRouteEvent.get();

            try {

                // Look at the Tag of this train event to determine what needs to
                // be updated in the routes
                final TrainTag trainTag = routeEvent.getTag();

                // If the trainrouteassignment Event is marked with a start trainTag, we need to generate a new trainrouteassignment
                // for this train
//                if (trainTag == TrainTagLiteral.START) {
//
//                    this.routePlanner.plan(routeEvent.toTrain());
//                }

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


    @Scheduled(fixedDelay = 1000)
    private void publishTrainVisit() {

        // If the queue of pending stations is empty, find all stations in the repository that
        // need to be published via trainVisit
        if (this.pendingTrainVisits.isEmpty()) {
            this.trainDestinationRepository
                    .findAllByCanBeVisitedIsTrueAndHasBeenVisitedIsFalse()
                    .forEach(trainDestination -> this.pendingTrainVisits.add(trainDestination.getId()));
        }

        final Long id = this.pendingTrainVisits.poll();
        if (id != null) {

            this.trainDestinationRepository
                    .findById(id)
                    .filter(td -> ! td.isHasBeenVisited() && td.isCanBeVisited())
                    .ifPresent(td -> {

                        this.processor.output().send(
                                MessageBuilder
                                        .withPayload(new TrainVisit(
                                                UUID.fromString(td.getTrainID()),
                                                URI.create(td.getTrainDockerRegistryURI()),
                                                UUID.fromString(td.getStationID())))
                                        .build()
                        );
                    });
        }
    }
}
