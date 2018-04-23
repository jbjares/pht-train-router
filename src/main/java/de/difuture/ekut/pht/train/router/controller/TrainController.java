package de.difuture.ekut.pht.train.router.controller;


import de.difuture.ekut.pht.lib.core.model.Route;
import de.difuture.ekut.pht.train.router.repository.traindestination.TrainDestination;
import de.difuture.ekut.pht.train.router.repository.traindestination.TrainDestinationRepository;
import de.difuture.ekut.pht.train.router.repository.trainroutes.TrainRoutes;
import de.difuture.ekut.pht.train.router.repository.trainroutes.TrainRoutesRepository;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Function;

@CrossOrigin
@RestController
@RequestMapping("/train")
public class TrainController {

    @Value
    private static final class Multiplicity {

        UUID stationID;
        Long trainDestinationID;

        private static Multiplicity of(final TrainDestination trainDestination) {

            return new Multiplicity(UUID.fromString(trainDestination.getStationID()), trainDestination.getId());
        }
    }


    private final TrainRoutesRepository trainRoutesRepository;
    private final TrainDestinationRepository trainDestinationRepository;

    @Autowired
    public TrainController(TrainRoutesRepository trainRoutesRepository,
                           TrainDestinationRepository trainDestinationRepository) {

        this.trainDestinationRepository = trainDestinationRepository;
        this.trainRoutesRepository = trainRoutesRepository;
    }


    /**
     * Adds next route to the list of routes, returns the new route Number.
     */
    private Long addNextRoute(final UUID trainID) {

        // Assign a new trainRoute number for this route and save to repository
        final TrainRoutes trainRoutes = this.trainRoutesRepository
                .findById(trainID)
                .orElse(new TrainRoutes(trainID));
        final List<Long> routes = trainRoutes.getRoutes();
        final Long nextRoute = routes.stream().mapToLong(Long::valueOf).max().orElse(0) + 1;
        routes.add(nextRoute);
        trainRoutes.setRoutes(routes);
        this.trainRoutesRepository.saveAndFlush(trainRoutes);
        return nextRoute;
    }


    /**
     * Creates a new TrainDestination for the Train with the given ID and the
     * provided route model
     *
     * @param trainID UUID of the train for which the TrainDestination should be generated
     * @param route The Route model for the newly created TrainDestination
     */
    private void createTrainDestination(UUID trainID, Route route) {

        final Long newRouteID = this.addNextRoute(trainID);
        // Each Node in the route belongs to one trainDestination
        final Map<Route.Node, TrainDestination> trainDestinations = new HashMap<>();

        // Define how to convert Route.Node to TrainDestination
        final Function<Route.Node, TrainDestination> convert = (node) ->

            trainDestinations.computeIfAbsent(node, (sourceNode) ->

                            TrainDestination.of(sourceNode.getStationID(), trainID, newRouteID)
            );

        // Link nodes in edges
        route.getEdges().forEach((edge) ->

            TrainDestination.link(
                    convert.apply(edge.getSource()),
                    convert.apply(edge.getTarget()))
        );
        route.getNodes().forEach(convert::apply);

        // Go through all TrainDestinations again and mark the ones with no incoming
        // edges (so the ones whose parent list is empty) that the TrainDestination
        // is ready to be processed
        // We need to save all root nodes. Note that a route does not
        // Necessarily need to be connected
        for (final TrainDestination trainDestination: trainDestinations.values()) {

            // If it does not have parents, it is a root node and we need to save it
            if (trainDestination.getParents().isEmpty()) {

                trainDestination.setCanBeVisited(true);
                this.trainDestinationRepository.save(trainDestination);
            }
        }
    }


    private Route.Node convertToNode(
            TrainDestination trainDestination,
            Map<Multiplicity, Integer> multiplicities,
            Map<Long, Route.Node> nodes) {

        return nodes.computeIfAbsent(trainDestination.getId(), (trainDestinationID) -> {

            final int multiplicity = multiplicities.computeIfAbsent(Multiplicity.of(trainDestination), (m) ->

                    multiplicities.values().stream().mapToInt(Integer::valueOf).max().orElse(0) + 1
            );

            return new Route.Node(
                    trainDestinationID,
                    UUID.fromString(trainDestination.getStationID()),
                    multiplicity);
        });
    }


    /**
     * Adds a new route to the train with the provided trainID
     */
    @RequestMapping(value = "/{trainID}", method = RequestMethod.POST, consumes = "application/json")
    public void addRoute(@PathVariable UUID trainID, @RequestBody Route route) {

        // TODO Verify that the route is valid, so it is a DAG and does not contain
        // TODO circles
        this.createTrainDestination(trainID, route);
    }

    /**
     * Adds a new route to the train with the provided trainID
     */
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public Iterable<TrainRoutes> getAllTrainRoutes() {

        return this.trainRoutesRepository.findAll();
    }


    /**
     * Fetches particular route
     *
     */

    @RequestMapping(value = "/{trainID}/{routeID}", method = RequestMethod.GET)
    public Route getRoute(@PathVariable UUID trainID, @PathVariable Long routeID) {


        final List<TrainDestination> trainDestinations = this.trainDestinationRepository
                .findAllByTrainIDAndRouteID(trainID.toString(), routeID);

        final Map<Multiplicity, Integer> multiplicities = new HashMap<>();
        final Map<Long, Route.Node> nodes = new HashMap<>();

        // The edge Set
        final Set<Route.Edge> edgeSet = new HashSet<>();

        for (final TrainDestination trainDestination : trainDestinations) {

            // Collect the nodes for this train destination, parents, and children
            final Route.Node head = convertToNode(trainDestination, multiplicities, nodes);

            // Add edges for children
            final List<TrainDestination> children = trainDestination.getChildren();
            if (children != null) {
                children.stream()
                        .map(x -> convertToNode(x, multiplicities, nodes))
                        .forEach( (child) ->

                                // Add a new Edge to the edge set (from head -> children)
                                edgeSet.add(new Route.Edge(head, child)));
            }
            // Add edges for parents
            final List<TrainDestination> parents = trainDestination.getParents();
            if (parents != null) {
                parents.stream()
                        .map(x -> convertToNode(x, multiplicities, nodes))
                        .forEach( (parent) ->

                                // Add a new Edge to the edge set (from head -> children)
                                edgeSet.add(new Route.Edge(parent, head)));
            }
        }
        return new Route(new HashSet<>(nodes.values()), edgeSet);
    }
}
