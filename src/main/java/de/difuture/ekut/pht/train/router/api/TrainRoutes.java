package de.difuture.ekut.pht.train.router.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public final class TrainRoutes {

    @JsonProperty("trainID")
    private UUID trainID;

    @JsonProperty("routes")
    private Set<Long> routes;
}
