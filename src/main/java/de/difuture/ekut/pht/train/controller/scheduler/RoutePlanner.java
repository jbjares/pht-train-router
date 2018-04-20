package de.difuture.ekut.pht.train.controller.scheduler;

import de.difuture.ekut.pht.train.controller.repository.station.Station;
import de.difuture.ekut.pht.train.controller.repository.station.StationRepository;
import de.difuture.ekut.pht.train.controller.repository.traindestination.TrainDestination;
import de.difuture.ekut.pht.train.controller.repository.traindestination.TrainDestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoutePlanner {

    // Route Planner needs to have access to the TrainDestinationRepository
    private final TrainDestinationRepository trainDestinationRepository;

    // Access to the Station Repository is needed to assemble the routes
    private final StationRepository stationRepository;

    @Autowired
    public RoutePlanner(
            TrainDestinationRepository trainDestinationRepository,
            StationRepository stationRepository) {

        this.trainDestinationRepository = trainDestinationRepository;
        this.stationRepository = stationRepository;
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
        final List<Station> enabledStations = this.stationRepository.findAllByEnabledIsTrue();
        Collections.shuffle(enabledStations);

        final Optional<TrainDestination> route = TrainDestination.of(enabledStations, trainID);
        if (route.isPresent()) {

            this.trainDestinationRepository.save(route.get());
            System.out.println("Route saved");
        }
    }
}
