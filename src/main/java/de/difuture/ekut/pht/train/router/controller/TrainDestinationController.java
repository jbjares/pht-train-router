package de.difuture.ekut.pht.train.router.controller;


import de.difuture.ekut.pht.train.router.api.NodeInfo;
import de.difuture.ekut.pht.train.router.client.StationOfficeClient;
import de.difuture.ekut.pht.train.router.repository.traindestination.TrainDestination;
import de.difuture.ekut.pht.train.router.repository.traindestination.TrainDestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@CrossOrigin
@RestController
@RequestMapping("/node")
public class TrainDestinationController {

    private final TrainDestinationRepository trainDestinationRepository;
    private final StationOfficeClient stationOfficeClient;

    @Autowired
    public TrainDestinationController(
            final TrainDestinationRepository trainDestinationRepository,
            final StationOfficeClient stationOfficeClient) {

        this.trainDestinationRepository = trainDestinationRepository;
        this.stationOfficeClient = stationOfficeClient;
    }

    @RequestMapping(
            method = RequestMethod.GET,
            produces = "application/json")
    public Iterable<NodeInfo> get(
            @RequestParam(value = "id") Set<Long> ids) {


        // Get Stations if we require this information, filter for the
        // stations that we actually care about
        // TODO How can one direclty collect to a map?
        final Map<UUID, String> stationNames = new HashMap<>();
        this.stationOfficeClient
                .getStations()
                .forEach(station -> {

                    stationNames.put(station.getStationID(), station.getStationName());
                });
        final Iterable<TrainDestination> trainDestinations =
                this.trainDestinationRepository.findAllById(ids);

        return StreamSupport.stream(trainDestinations.spliterator() , false)
                .map((trainDestination -> {

                    final UUID stationID = UUID.fromString(trainDestination.getStationID());
                    return new NodeInfo(
                            trainDestination.getId(),
                            stationID,
                            stationNames.getOrDefault(stationID,"unknown"),
                            trainDestination.isCanBeVisited(),
                            trainDestination.isHasBeenVisited());

                })).collect(Collectors.toList());
    }
}
