package de.difuture.ekut.pht.train.controller.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.difuture.ekut.pht.train.controller.model.Station;
import lombok.Value;

@Value
public class StationResponse {

    @JsonProperty("stations")
    Iterable<Station> stations;
}
