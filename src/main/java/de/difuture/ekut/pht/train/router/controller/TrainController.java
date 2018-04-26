package de.difuture.ekut.pht.train.router.controller;


import de.difuture.ekut.pht.lib.core.api.Route;
import de.difuture.ekut.pht.lib.core.neo4j.entity.RouteEntity;
import de.difuture.ekut.pht.lib.core.neo4j.entity.TrainEntity;
import de.difuture.ekut.pht.train.router.repository.RouteEntityRepository;
import de.difuture.ekut.pht.train.router.repository.TrainDestinationRepository;
import de.difuture.ekut.pht.train.router.repository.TrainEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/train")
public class TrainController {

    /*
    @Value
    private static final class Multiplicity {

        UUID stationID;
        Long trainDestinationID;

        private static Multiplicity of(final TrainDestination trainDestination) {

            return new Multiplicity(UUID.fromString(trainDestination.getStationID()), trainDestination.getId());
        }
    }
    */

    private final TrainDestinationRepository trainDestinationRepository;
    private final TrainEntityRepository trainEntityRepository;
    private final RouteEntityRepository routeEntityRepository;

    @Autowired
    public TrainController(
            TrainDestinationRepository trainDestinationRepository,
            TrainEntityRepository trainEntityRepository,
            RouteEntityRepository routeEntityRepository) {

        this.trainDestinationRepository = trainDestinationRepository;
        this.trainEntityRepository = trainEntityRepository;
        this.routeEntityRepository = routeEntityRepository;
    }


    /**
     * Creates a new TrainDestination for the Train with the given ID and the
     * provided route api
     *
     * @param trainEntity UUID of the train for which the TrainDestination should be generated
     * @param route The Route api for the newly created TrainDestination
     */
    private void createRoute(TrainEntity trainEntity, Route route) {

        // Create a new RouteEntity Node
        final RouteEntity routeEntity = new RouteEntity(trainEntity);
        trainEntity.getRoutes().add(routeEntity);

        /*
        // Each Node in the route belongs to one trainDestination
        final Map<Route.Node, TrainDestination> trainDestinations = new HashMap<>();

        // Define how to convert Route.Node to TrainDestination
        final Function<Route.Node, TrainDestination> convert = (node) ->

            trainDestinations.computeIfAbsent(node, (sourceNode) ->

                            TrainDestination.of(sourceNode.getStationID(), trainID, nextRouteID)
            );

        // Link nodes in edges
        route.getEdges().forEach((edge) ->

            TrainDestination.link(
                    convert.apply(edge.getSource()),
                    convert.apply(edge.getTarget()))
        );
        route.getNodes().forEach(convert::apply);

        // We need to save all root nodes. Note that a route does not
        // Necessarily need to be connected
        trainDestinations.values().forEach(trainDestination -> {

            if (trainDestination.getParents().isEmpty()) {

                // A node without parents is a root node
                trainDestination.setRoot(true);
                this.trainDestinationRepository.save(trainDestination);
            }
        });*/
        this.routeEntityRepository.save(routeEntity);
        this.trainEntityRepository.save(trainEntity);
    }

    /*
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
    */


    /**
     * Adds a new route to the train with the provided trainID.
     */
    @RequestMapping(value = "/{trainID}", method = RequestMethod.POST, consumes = "application/json")
    public void addRoute(@PathVariable Long trainID, @RequestBody Route route) {

        // Adds a new Route to the train
        this.trainEntityRepository.findById(trainID)
                .ifPresent(trainEntity -> this.createRoute(trainEntity, route));
    }

    /**
     * Adds a new route to the train with the provided trainID
     */
    /*
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public Iterable<TrainRoutes> getAllTrainRoutes() {

        final Map<UUID, Set<Long>> trainRoutes = new HashMap<>();
        this.trainDestinationRepository.findAll().forEach(trainDestination ->

            trainRoutes.compute(UUID.fromString(trainDestination.getTrainID()),
                    (trainID, previousRoutes) -> {

                        final Set<Long> result
                                = previousRoutes == null ? new HashSet<>() : previousRoutes;
                        result.add(trainDestination.getRouteID());
                        return result;
                    })
        );
        return trainRoutes.entrySet().stream().map((entry) ->
            new TrainRoutes(entry.getKey(), entry.getValue())
        ).collect(Collectors.toList());
    }
    */
    /**
     * Fetches particular route
     *
     */

    /*
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
    */
}
