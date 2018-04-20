package de.difuture.ekut.pht.train.controller.controller;


import de.difuture.ekut.pht.train.controller.repository.station.Station;
import de.difuture.ekut.pht.train.controller.repository.station.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.Instant;


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
    ResponseEntity<?> ping(@Valid @RequestBody Station station) {

        final Station modifiedStation = this.stationRepository
                .findStationByUri(station.getUri())
                .orElse(station);

        // Reenable the station and set the last ping
        modifiedStation.setLast_ping(Instant.now());
        modifiedStation.setEnabled(true);

        final Station result = this.stationRepository.saveAndFlush(modifiedStation);
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
