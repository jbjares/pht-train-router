package de.difuture.ekut.pht.train.router.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Node {

    @JsonProperty("trainDestinationID")
    private Long trainDestinationID;

    @JsonProperty("canBeVisited")
    private boolean canBeVisited;

    @JsonProperty("hasBeenVisited")
    private boolean hasBeenVisited;

    @JsonProperty("trainRegistryURI")
    private URI trainRegistryURI;

    @JsonProperty("trainID")
    private Long trainID;

    @JsonProperty("stationName")
    private String stationName;
}
