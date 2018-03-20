package de.difuture.ekut.pht.trainrouter.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.difuture.ekut.pht.trainrouter.model.TrainRoute;
import lombok.Value;

@Value
public class RouteResponse {

    @JsonProperty("routes")
    Iterable<TrainRoute> routes;
}
