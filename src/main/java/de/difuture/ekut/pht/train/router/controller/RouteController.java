package de.difuture.ekut.pht.train.router.controller;

import de.difuture.ekut.pht.lib.core.api.APIRoute;
import de.difuture.ekut.pht.train.router.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@CrossOrigin
@RestController
@RequestMapping("/route")
public class RouteController {

    private static final ResponseEntity<?> NOT_FOUND = ResponseEntity.notFound().build();
    private static final ResponseEntity<?> OK = ResponseEntity.ok().build();


    private final RouteService routeService;

    @Autowired
    public RouteController(RouteService routeService) {

        this.routeService = routeService;
    }

    @RequestMapping(value = "/{routeID}", method = RequestMethod.GET)
    public ResponseEntity<?> getRoute(@PathVariable Long routeID) {

        final Optional<APIRoute> results =  this.routeService
                .getRoute(routeID);

        if ( ! results.isPresent()) {

            return NOT_FOUND;
        }
        return ResponseEntity.ok(results.get());
    }


    @RequestMapping(value = "/{routeID}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteRoute(@PathVariable Long routeID) {

        this.routeService.deleteRoute(routeID);
        return OK;
    }
}
