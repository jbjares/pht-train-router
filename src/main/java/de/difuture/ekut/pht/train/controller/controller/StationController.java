package de.difuture.ekut.pht.train.controller.controller;


import de.difuture.ekut.pht.train.controller.api.StationAlreadyExistsException;
import de.difuture.ekut.pht.train.controller.repository.station.Station;
import de.difuture.ekut.pht.train.controller.repository.station.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;


/**
 * Provides the handlers for the /station route
 *
 * @author Lukas Zimmermann
 */
@RestController
@RequestMapping("/station")
public class StationController {

    private final StationRepository stationRepository;

    @Autowired
    public StationController(StationRepository stationRepository) {

        this.stationRepository = stationRepository;
    }

    /**
     * Registers the station at the train controller application.
     * The station is just represented as a URI.
     *
     * @param station The station that should be created.
     */
    @RequestMapping(method = RequestMethod.POST)
    ResponseEntity<?> add(@Valid @RequestBody Station station) {

        if (this.stationRepository
                .findStationByUri(station.getUri())
                .isPresent()) {

            throw new StationAlreadyExistsException(station);
        }
        final Station result = this.stationRepository.saveAndFlush(station);
        final URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @RequestMapping(method = RequestMethod.GET)
    Iterable<Station> getAll() {

        return this.stationRepository.findAll();
    }
}
