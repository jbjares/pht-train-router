package de.difuture.ekut.pht.train.router.service;


import de.difuture.ekut.pht.train.router.client.StationOfficeClient;
import de.difuture.ekut.pht.train.router.client.TrainOfficeClient;
import de.difuture.ekut.pht.train.router.repository.TrainDestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Component scans events happening on the trainroutes and
 *
 */
//@Component
public class RouteEventProcessor {

    /*
    // Needed for fetching unprocessed trainroutes events
    private final RouteEventRepository routeEventRepository;

    private final TrainDestinationRepository trainDestinationRepository;

    // Needed for trainroutes planning of START trains
    private final StationOfficeClient stationOfficeClient;
    private final TrainOfficeClient trainOfficeClient;

    private final Processor processor;

    private final Queue<Long> pendingTrainVisits;


    @Autowired
    public RouteEventProcessor(
            RouteEventRepository routeEventRepository,
            TrainDestinationRepository trainDestinationRepository,
            StationOfficeClient stationOfficeClient,
            TrainOfficeClient trainOfficeClient,
            Processor processor) {

        this.routeEventRepository = routeEventRepository;
        this.trainDestinationRepository = trainDestinationRepository;
        this.stationOfficeClient = stationOfficeClient;
        this.trainOfficeClient = trainOfficeClient;
        this.processor = processor;

        this.pendingTrainVisits = new LinkedBlockingQueue<>();
    }


    private void checkTrains() {

        // Find all trains that are Root and that can not yet be published
        this.trainDestinationRepository
                .findAllByRootIsTrueAndCanBeVisitedIsFalse()
                .forEach(trainDestination -> {

                    // Get the train from the TrainOfficeClient
                    this.trainOfficeClient
                            .getTrain(UUID.fromString(trainDestination.getTrainID()))
                            .filter(train -> train.getTrainRegistryURI() != null)
                            .ifPresent(train -> {

                                trainDestination.setCanBeVisited(true);
                                trainDestination.setDockerRegistryURI(train.getTrainRegistryURI());
                                this.trainDestinationRepository.save(trainDestination);
                            });
                });
    }


    // Processes one single trainroutes event that has not been visited before
    @Scheduled(fixedDelay = 1000)
    private void processRouteEvents() {

        this.checkTrains();

        final Optional<RouteEvent> existingRouteEvent
                = this.routeEventRepository.getFirstByProcessedIsFalse();

        // Only do something if there is an unprocessed trainroutes event
        if (existingRouteEvent.isPresent()) {

            final RouteEvent routeEvent = existingRouteEvent.get();

            try {

                // Look at the Tag of this train event to determine what needs to
                // be updated in the routes
                final TrainTag trainTag = routeEvent.getTag();

                // If the trainroutes Event is marked with a start trainTag, we need to generate a new trainroutes
                // for this train
                if (trainTag == TrainTagLiteral.START) {

                    // Route nodes of this train can now be visited
                    this.trainDestinationRepository
                            .findAllByTrainIDAndRootIsTrue(
                                    routeEvent.getTrainID().toString()).forEach(trainDestination -> {

                                        trainDestination.setCanBeVisited(true);
                                        trainDestination.setDockerRegistryURI(routeEvent.getDockerRegistryURI());
                                        this.trainDestinationRepository.save(trainDestination);
                    });
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

                        // Get the train from the

                        this.processor.output().send(
                                MessageBuilder
                                        .withPayload(new TrainVisit(
                                                UUID.fromString(td.getTrainID()),
                                                td.getDockerRegistryURI(),
                                                UUID.fromString(td.getStationID()),
                                                td.getRouteID()))
                                        .build()
                        );
                    });
        }
    }
    */
}
