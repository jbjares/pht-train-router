package de.difuture.ekut.pht.train.router.controller;


import de.difuture.ekut.pht.lib.core.api.APIRoute;
import de.difuture.ekut.pht.train.router.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/train")
public class TrainController {

    private static final ResponseEntity<?> BAD_REQUEST = ResponseEntity.badRequest().build();

    private final RouteService routeService;

    @Autowired
    public TrainController(RouteService routeService) {

        this.routeService = routeService;
    }


    /*
    @Value
    private static final class Multiplicity {

        UUID stationID;
        Long trainDestinationID;

        private static Multiplicity of(final TrainDestination trainDestination) {

            return new Multiplicity(UUID.fromString(trainDestination.getStationID()), trainDestination.getTrainDestinationID());
        }
    }
    */






    /*
    private APIRoute.Node convertToNode(
            TrainDestination trainDestination,
            Map<Multiplicity, Integer> multiplicities,
            Map<Long, APIRoute.Node> nodes) {

        return nodes.computeIfAbsent(trainDestination.getTrainDestinationID(), (trainDestinationID) -> {

            final int multiplicity = multiplicities.computeIfAbsent(Multiplicity.of(trainDestination), (m) ->

                    multiplicities.values().stream().mapToInt(Integer::valueOf).max().orElse(0) + 1
            );

            return new APIRoute.Node(
                    trainDestinationID,
                    UUID.fromString(trainDestination.getStationID()),
                    multiplicity);
        });
    }
    */


    /**
     * Adds a new APIRoute to the train with the provided trainID.
     */
    @RequestMapping(value = "/{trainID}", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> addRoute(@PathVariable Long trainID, @RequestBody APIRoute apiRoute) {

        // There is a node with more than one parent
        if ( ! apiRoute.hasOneParent()) {

            return BAD_REQUEST;
        }
        this.routeService.createRoute(trainID, apiRoute);
        return ResponseEntity.ok().build();
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
    public APIRoute getRoute(@PathVariable UUID trainID, @PathVariable Long routeID) {


        final List<TrainDestination> trainDestinations = this.trainDestinationRepository
                .findAllByTrainIDAndRouteID(trainID.toString(), routeID);

        final Map<Multiplicity, Integer> multiplicities = new HashMap<>();
        final Map<Long, APIRoute.Node> nodes = new HashMap<>();

        // The edge Set
        final Set<APIRoute.Edge> edgeSet = new HashSet<>();

        for (final TrainDestination trainDestination : trainDestinations) {

            // Collect the nodes for this train destination, parents, and children
            final APIRoute.Node head = convertToNode(trainDestination, multiplicities, nodes);

            // Add edges for children
            final List<TrainDestination> children = trainDestination.getChildren();
            if (children != null) {
                children.stream()
                        .map(x -> convertToNode(x, multiplicities, nodes))
                        .forEach( (child) ->

                                // Add a new Edge to the edge set (from head -> children)
                                edgeSet.add(new APIRoute.Edge(head, child)));
            }
            // Add edges for parents
            final List<TrainDestination> parents = trainDestination.getParents();
            if (parents != null) {
                parents.stream()
                        .map(x -> convertToNode(x, multiplicities, nodes))
                        .forEach( (parent) ->

                                // Add a new Edge to the edge set (from head -> children)
                                edgeSet.add(new APIRoute.Edge(parent, head)));
            }
        }
        return new APIRoute(new HashSet<>(nodes.values()), edgeSet);
    }
    */
}
