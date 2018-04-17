package de.difuture.ekut.pht.train.controller.api;

import de.difuture.ekut.pht.train.controller.repository.Station;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.CONFLICT, reason="Station has already been registered")  // 409
public class StationAlreadyExistsException extends RuntimeException {

    private final Station station;

    public StationAlreadyExistsException(Station station) {

        super("Station with URI " + station.getUri() + " already exists");
        this.station = station;
    }

    public Station getStation() {

        return this.station;
    }
}
