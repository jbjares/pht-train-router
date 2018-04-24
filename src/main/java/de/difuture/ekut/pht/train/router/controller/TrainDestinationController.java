package de.difuture.ekut.pht.train.router.controller;


import de.difuture.ekut.pht.lib.core.model.Station;
import de.difuture.ekut.pht.train.router.api.InformationItem;
import de.difuture.ekut.pht.train.router.api.NodeInfo;
import de.difuture.ekut.pht.train.router.api.NodeInfoRequest;
import de.difuture.ekut.pht.train.router.client.StationClient;
import de.difuture.ekut.pht.train.router.repository.traindestination.TrainDestination;
import de.difuture.ekut.pht.train.router.repository.traindestination.TrainDestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/node")
public class TrainDestinationController {

    private final TrainDestinationRepository trainDestinationRepository;
    private final StationClient stationClient;

    @Autowired
    public TrainDestinationController(
            final TrainDestinationRepository trainDestinationRepository,
            final StationClient stationClient) {

        this.trainDestinationRepository = trainDestinationRepository;
        this.stationClient = stationClient;
    }

    @RequestMapping(
            method = RequestMethod.GET,
            consumes = "application/json",
            produces = "application/json")
    public Iterable<NodeInfo> get(@Valid @RequestBody NodeInfoRequest nodeInfoRequest) {

        // Assemble NodeInfo from TrainDestination
        final Set<Long> nodeIDs = nodeInfoRequest.getNodes();
        final Set<InformationItem> infos = nodeInfoRequest.getInfos();

        final Iterable<TrainDestination> trainDestinations =
                this.trainDestinationRepository.findAllById(nodeIDs);

        // Get Stations if we require this information
        final List<Station> stations = InformationItem.requiresStationOffice(infos) ?
                this.stationClient.getStations() : null;

        if (stations != null) {

            System.out.println("Retrieved information on " + stations.size() + " stations");
        }

        /*
        StreamSupport.stream(trainDestinations.spliterator() , false)
                .map((trainDestination -> {



                }))
        */
        // Map each retrieved node to a corresponding NodeInfo object

        return null;
    }
}
