package de.difuture.ekut.pht.train.router.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public final class Route {

    @JsonProperty("nodes")
    private List<UUID> nodes;

    @JsonProperty("edges")
    private List<Edge> edges;

    @Data
    @NoArgsConstructor
    public static final class Edge {

        @JsonProperty("source")
        private UUID source;

        @JsonProperty("target")
        private UUID target;
    }
}
