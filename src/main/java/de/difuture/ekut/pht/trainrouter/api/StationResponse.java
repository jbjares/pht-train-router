package de.difuture.ekut.pht.trainrouter.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.difuture.ekut.pht.trainrouter.model.Station;
import lombok.Value;

@Value
public class StationResponse {

    @JsonProperty("stations")
    Iterable<Station> stations;
}
