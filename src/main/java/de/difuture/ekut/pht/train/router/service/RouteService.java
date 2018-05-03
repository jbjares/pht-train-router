package de.difuture.ekut.pht.train.router.service;

import de.difuture.ekut.pht.lib.core.api.APIRoute;
import de.difuture.ekut.pht.lib.core.neo4j.entity.Route;
import de.difuture.ekut.pht.lib.core.neo4j.entity.Station;
import de.difuture.ekut.pht.lib.core.neo4j.entity.Train;
import de.difuture.ekut.pht.lib.core.neo4j.entity.TrainDestination;
import de.difuture.ekut.pht.train.router.repository.RouteRepository;
import de.difuture.ekut.pht.train.router.repository.StationRepository;
import de.difuture.ekut.pht.train.router.repository.TrainDestinationRepository;
import de.difuture.ekut.pht.train.router.repository.TrainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RouteService {

    private final TrainDestinationRepository trainDestinationRepository;
    private final TrainRepository trainRepository;
    private final RouteRepository routeEntityRepository;
    private final StationRepository stationRepository;

    @Autowired
    public RouteService(
            TrainDestinationRepository trainDestinationRepository,
            TrainRepository trainRepository,
            RouteRepository routeEntityRepository,
            StationRepository stationRepository) {

        this.trainDestinationRepository = trainDestinationRepository;
        this.trainRepository = trainRepository;
        this.routeEntityRepository = routeEntityRepository;
        this.stationRepository = stationRepository;
    }


    public Optional<APIRoute> getRoute(Long routeID) {

        final Iterable<TrainDestination> route = this.trainDestinationRepository.findAllById(
                this.routeEntityRepository
                        .getTrainDestinationIDs(routeID)
                        .stream()
                        .map(TrainDestination::getTrainDestinationID)
                        .collect(Collectors.toSet()), 2);

        // Keep track of the number of stationIDs encountered.
        final Map<Long, Integer> stationIDs = new HashMap<>();

        // Assign each TrainDestination ID the respective node
        final Map<Long, APIRoute.Node> nodes = new HashMap<>();

        final Function<TrainDestination, APIRoute.Node> convertToNode = (trainDestination) ->

            nodes.computeIfAbsent(trainDestination.getTrainDestinationID(), (tdID) -> {

                final Station station =  trainDestination.getStation();
                final Long stationID = station.getStationID();
                final Integer m =  stationIDs.compute(stationID,

                        (sID, number) ->

                                (number == null) ? 1 : number + 1
                );
                // ID of the API route will be the same as the TrainDestination id
                return new APIRoute.Node(tdID, stationID, m);
            });

        final Set<APIRoute.Edge> edges = new HashSet<>();
        route.forEach(trainDestination -> {

            // Transform root node and children to nodes
            final APIRoute.Node root = convertToNode.apply(trainDestination);
            trainDestination.getChildren().forEach(child ->

                edges.add(new APIRoute.Edge(root, convertToNode.apply(child)))
            );
        });
        return Optional.of(new APIRoute(new HashSet<>(nodes.values()), edges));
    }


    List<TrainDestination> getPendingTrainDestinations() {

        return this.trainDestinationRepository.findAllByCanBeVisitedIsTrueAndHasBeenVisitedIsFalse();
    }

    public Optional<Set<Long>> getTrainRouteIDs(Long trainID) {

        return this.trainRepository
                .findById(trainID)
                .map(train ->
                        train.getRoutes()
                                .stream()
                                .map(Route::getRouteID)
                                .collect(Collectors.toSet())
                );
    }


    public void enableRoutes(Long trainID) {

        // Adds a new APIRoute to the train
        this.trainRepository
                .findById(trainID, 3)  // Need to resolve to depth 3 to get the train destinations
                .map(train ->
                    train.getRoutes()
                            .stream()
                            .flatMap(route -> route.getStarts().stream())
                            .peek(trainDestination ->

                                trainDestination.setCanBeVisited(true)

                            ).collect(Collectors.toList()))
                .ifPresent(this.trainDestinationRepository::saveAll);
    }


    public void visitTrainDestination(Long trainDestinationID) {

        this.trainDestinationRepository
                .findById(trainDestinationID, 1)
                .ifPresent(trainDestination -> {

                    trainDestination.setHasBeenVisited(true);
                    trainDestination.getChildren().forEach(child -> child.setCanBeVisited(true));
                    this.trainDestinationRepository.save(trainDestination, 1);
                });
    }


    /**
     * Creates a new TrainDestination for the APITrain with the given ID and the
     * provided APIRoute api
     *
     * @param apiRoute The APIRoute api for the newly created TrainDestination
     */
    public void createRoute(Long trainID, APIRoute apiRoute) {

        // Adds a new APIRoute to the train
        final Optional<Train> otrain = this.trainRepository.findById(trainID);

        if ( ! otrain.isPresent()) {

            return;
        }
        final Train trainEntity = otrain.get();
        final Map<Long, Station> stations = new HashMap<>();

        // First, collect all the stations that appear in this route
        this.stationRepository
                .findAllById(
                        apiRoute.getNodes()
                                .stream()
                                .map(APIRoute.Node::getStationID)
                                .collect(Collectors.toList()))
                .forEach(station ->
                        stations.put(station.getStationID(), station)
                );
        // Create a new Route Node
        final Route route = new Route(trainEntity);
        trainEntity.getRoutes().add(route);

        // Each Node in the APIRoute belongs to one trainDestination
        final Map<APIRoute.Node, TrainDestination> trainDestinations = new HashMap<>();

        // Define how to convert APIRoute.Node to TrainDestination
        final Function<APIRoute.Node, TrainDestination> convert = (node) ->

                trainDestinations.computeIfAbsent(node, (sourceNode) ->

                        this.trainDestinationRepository.save(

                                new TrainDestination(
                                        stations.get(sourceNode.getStationID()), trainEntity)
                        )
                );

        // Link all edges
        apiRoute.getEdges().forEach((edge) ->

                    TrainDestination.link(
                            convert.apply(edge.getSource()),
                            convert.apply(edge.getTarget()))
        );
        apiRoute.getNodes().forEach(convert::apply);

        trainDestinations.forEach((node, trainDestination) -> {

            if (trainDestination.isRoot()) {

                trainDestination.setCanBeVisited(trainEntity.getTrainRegistryURI() != null);
                route.getStarts().add(trainDestination);
            }
            this.trainDestinationRepository.save(trainDestination);
        });

        this.routeEntityRepository.save(route);
        this.trainRepository.save(trainEntity);
    }
}
