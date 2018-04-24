package de.difuture.ekut.pht.train.router.api;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Set;


/**
 *
 * Request for retrieving information on nodes (TrainDestination)
 * on routes which can be sent to this service
 *
 * @author Lukas Zimmermann
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class NodeInfoRequest {

    @JsonProperty("nodes")
    @NotNull
    private Set<Long> nodes;

    @JsonProperty("infos")
    private Set<InformationItem> infos;
}
