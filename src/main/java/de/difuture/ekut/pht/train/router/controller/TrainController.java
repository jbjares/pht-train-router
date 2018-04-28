package de.difuture.ekut.pht.train.router.controller;


import de.difuture.ekut.pht.lib.core.api.APIRoute;
import de.difuture.ekut.pht.train.router.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;


@CrossOrigin
@RestController
@RequestMapping("/train")
public class TrainController {

    private static final ResponseEntity<?> BAD_REQUEST = ResponseEntity.badRequest().build();
    private static final ResponseEntity<?> NOT_FOUND = ResponseEntity.notFound().build();

    private final RouteService routeService;

    @Autowired
    public TrainController(RouteService routeService) {

        this.routeService = routeService;
    }

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
     * Gets a list of all train routes, as ids. for a particular train
     */
    @RequestMapping(value = "/{trainID}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getRoutes(@PathVariable Long trainID) {

        final Optional<Set<Long>> results =  this.routeService
                .getTrainRouteIDs(trainID);
        if ( ! results.isPresent()) {

            return NOT_FOUND;
        }
        return ResponseEntity.ok(results.get());
    }
}
