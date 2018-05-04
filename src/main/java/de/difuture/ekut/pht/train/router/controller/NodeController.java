package de.difuture.ekut.pht.train.router.controller;


import de.difuture.ekut.pht.lib.core.neo4j.entity.Train;
import de.difuture.ekut.pht.train.router.model.Node;
import de.difuture.ekut.pht.train.router.repository.TrainDestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.StreamSupport;


@CrossOrigin
@RestController
@RequestMapping("/node")
public class NodeController {

    private final TrainDestinationRepository trainDestinationRepository;

    @Autowired
    public NodeController(
            final TrainDestinationRepository trainDestinationRepository) {

        this.trainDestinationRepository = trainDestinationRepository;
    }


    @RequestMapping(
            method = RequestMethod.GET,
            produces = "application/json")
    public Map<Long, Node> get(
            @RequestParam(value = "id") Set<Long> ids) {

        final Map<Long, Node> result = new HashMap<>();

        // Find the trainDestinations
        StreamSupport.stream(this.trainDestinationRepository.findAllById(ids, 2).spliterator(), false )
                .filter(trainDestination -> trainDestination.getTrain()   != null &&
                                            trainDestination.getStation() != null)
                .forEach(trainDestination -> {

                    final Train train = trainDestination.getTrain();
                    final Long trainDestinationID = trainDestination.getTrainDestinationID();

                    final Node node = new Node(
                            trainDestinationID,
                            trainDestination.isCanBeVisited(),
                            trainDestination.isHasBeenVisited(),
                            train.getTrainRegistryURI(),
                            train.getTrainID(),
                            trainDestination.getStation().getStationName());
                    result.put(trainDestinationID, node);
                });
        return result;
    }
}
