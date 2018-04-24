package de.difuture.ekut.pht.train.router.api;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class NodeInfo {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("stationID")
    private UUID stationID;

    @JsonProperty("stationName")
    private String stationName;

    @JsonProperty("canBeVisited")
    private boolean canBeVisited;

    @JsonProperty("hasBeenVisited")
    private boolean hasBeenVisited;
}
