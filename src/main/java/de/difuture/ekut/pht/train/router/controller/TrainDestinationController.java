package de.difuture.ekut.pht.train.router.controller;


import de.difuture.ekut.pht.train.router.repository.TrainDestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



@CrossOrigin
@RestController
@RequestMapping("/node")
public class TrainDestinationController {

    private final TrainDestinationRepository trainDestinationRepository;

    @Autowired
    public TrainDestinationController(
            final TrainDestinationRepository trainDestinationRepository) {

        this.trainDestinationRepository = trainDestinationRepository;
    }

    /*
    @RequestMapping(
            method = RequestMethod.GET,
            produces = "application/json")
    public Iterable<NodeInfo> get(
            @RequestParam(value = "trainDestinationID") Set<Long> ids) {


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
                            trainDestination.getTrainDestinationID(),
                            stationID,
                            stationNames.getOrDefault(stationID,"unknown"),
                            trainDestination.isCanBeVisited(),
                            trainDestination.isHasBeenVisited());

                })).collect(Collectors.toList());
    }
    */
}
