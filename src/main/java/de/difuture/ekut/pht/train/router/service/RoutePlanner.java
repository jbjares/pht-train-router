package de.difuture.ekut.pht.train.router.service;

import de.difuture.ekut.pht.lib.core.messages.TrainVisit;
import de.difuture.ekut.pht.lib.core.model.Station;
import de.difuture.ekut.pht.lib.core.model.Train;
import de.difuture.ekut.pht.train.router.client.StationClient;
import de.difuture.ekut.pht.train.router.repository.traindestination.TrainDestination;
import de.difuture.ekut.pht.train.router.repository.traindestination.TrainDestinationRepository;
import de.difuture.ekut.pht.train.router.repository.trainrouteassignment.TrainRouteAssignment;
import de.difuture.ekut.pht.train.router.repository.trainrouteassignment.TrainRouteAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class RoutePlanner {

    // TrainRoute Planner needs to have access to the TrainDestinationRepository
    private final TrainDestinationRepository trainDestinationRepository;

    private final TrainRouteAssignmentRepository trainRouteAssignmentRepository;

    // Access to the Processer output to publish available routes
    private final Processor processor;

    // Client for getting list with all station
    private final StationClient stationClient;

    private final Queue<Long> pendingTrainVisits;

    @Autowired
    public RoutePlanner(
            TrainDestinationRepository trainDestinationRepository,
            TrainRouteAssignmentRepository trainRouteAssignmentRepository,
            StationClient stationClient,
            Processor processor) {

        this.trainDestinationRepository = trainDestinationRepository;
        this.trainRouteAssignmentRepository = trainRouteAssignmentRepository;
        this.stationClient = stationClient;
        this.processor = processor;
        this.pendingTrainVisits = new LinkedBlockingQueue<>();
    }

    private List<Station> getStations() {

        return this.stationClient.getStations();
    }


    /**
     * Plans a trainrouteassignment for the train with the given ID.
     *
     */
    void plan(final Train train) {

        // TODO
        // Check whether there is already a planned route for the
        // train, if not, plan a linear route
        this.linearRandom(train, 1L);
    }

    private void linearRandom(final Train train, Long routeID) {

        // Get all stations that are active
        final List<Station> stations = this.getStations();
        Collections.shuffle(stations);
        //TrainDestination.of(stations, train)
        //    .ifPresent(this.trainDestinationRepository::save);
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
