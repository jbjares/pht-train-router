package de.difuture.ekut.pht.train.controller.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.difuture.ekut.pht.train.controller.model.TrainRoute;
import lombok.Value;

@Value
public class RouteResponse {

    @JsonProperty("routes")
    Iterable<TrainRoute> routes;
}
