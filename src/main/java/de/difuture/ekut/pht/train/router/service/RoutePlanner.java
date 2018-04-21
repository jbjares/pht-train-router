package de.difuture.ekut.pht.train.router.service;

import de.difuture.ekut.pht.lib.core.messages.TrainVisit;
import de.difuture.ekut.pht.lib.core.model.Station;
import de.difuture.ekut.pht.train.router.client.StationClient;
import de.difuture.ekut.pht.train.router.repository.traindestination.TrainDestination;
import de.difuture.ekut.pht.train.router.repository.traindestination.TrainDestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class RoutePlanner {

    // Route Planner needs to have access to the TrainDestinationRepository
    private final TrainDestinationRepository trainDestinationRepository;

    // Access to the Processer output to publish available routes
    private final Processor processor;

    // Client for getting list with all station
    private final StationClient stationClient;

    private final Queue<Long> pendingTrainVisits;

    @Autowired
    public RoutePlanner(
            TrainDestinationRepository trainDestinationRepository,
            StationClient stationClient,
            Processor processor) {

        this.trainDestinationRepository = trainDestinationRepository;
        this.stationClient = stationClient;
        this.processor = processor;
        this.pendingTrainVisits = new LinkedBlockingQueue<>();
    }

    private List<Station> getStations() {

        return this.stationClient.getStations();
    }

    /**
     * Plans a route for the train with the given ID.
     *
     *
     * @param trainID
     */
    public void plan(final UUID trainID) {

        // TODO Only linearRandom currently supported
        this.linearRandom(trainID);
    }

    private void linearRandom(final UUID trainID) {

        // Get all stations that are active
        final List<Station> stations = this.getStations();
        Collections.shuffle(stations);
        TrainDestination.of(stations, trainID)
            .ifPresent(this.trainDestinationRepository::save);
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
                                                UUID.fromString(td.getStationID())))
                                        .build()
                        );
                    });
        }
    }
}
