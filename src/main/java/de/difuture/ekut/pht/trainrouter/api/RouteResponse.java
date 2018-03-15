package de.difuture.ekut.pht.trainrouter.api;

import de.difuture.ekut.pht.trainrouter.model.TrainRoute;
import lombok.Value;

@Value
public class RouteResponse {

    Iterable<TrainRoute> routes;
}
