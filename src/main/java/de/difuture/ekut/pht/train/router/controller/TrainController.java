package de.difuture.ekut.pht.train.router.controller;


import de.difuture.ekut.pht.lib.core.model.Route;
import de.difuture.ekut.pht.train.router.repository.traindestination.TrainDestination;
import de.difuture.ekut.pht.train.router.repository.traindestination.TrainDestinationRepository;
import de.difuture.ekut.pht.train.router.repository.trainroutes.TrainRoutes;
import de.difuture.ekut.pht.train.router.repository.trainroutes.TrainRoutesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/train")
public class TrainController {

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

        // Assign a new trainRoute number for this route and save to repositor
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

        final List<Route.Edge> edges = route.getEdges();
        // Each Node in the route belongs to one trainDestination
        final Map<Route.Node, TrainDestination> trainDestinations = new HashMap<>();

        // This set is used for discarding edges that appear multiple times
        final Set<Route.Edge> edgeSet = new HashSet<>();

        for (final Route.Edge edge : edges) {

            // Skip if we have already encountered this edge
            if (edgeSet.contains(edge)) {

                continue;
            }
            edgeSet.add(edge);

            System.out.println("PROCESSING EDGE " + edge);

            // Compute the trainDestination from the source node if absent until now
            final TrainDestination sourceDestination
                    = trainDestinations.computeIfAbsent(edge.getSource(), (sourceNode) ->

                TrainDestination.of(sourceNode.getId(), trainID, newRouteID)
            );
            final TrainDestination targetDestination
                    = trainDestinations.computeIfAbsent(edge.getTarget(), (targetNode) ->

               TrainDestination.of(targetNode.getId(), trainID, newRouteID)
            );
            // Establish the relationship between the source and target node for the database

            System.out.println("LINKING: " + sourceDestination.getStationID() + " and " + targetDestination.getStationID());
            TrainDestination.link(sourceDestination, targetDestination);
        }
        // Go through all TrainDestinations again and mark the ones with no incoming
        // edges (so the ones whose parent list is empty) that the TrainDestination
        // is ready to be processed
        TrainDestination result = null;
        for (final TrainDestination trainDestination: trainDestinations.values()) {

            if (trainDestination.getParents().isEmpty()) {

                result = trainDestination;
                trainDestination.setCanBeVisited(true);
            }
        }
        // Safe the result back to persistence
        if (result != null) {

            this.trainDestinationRepository.save(result);
        }
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
}
