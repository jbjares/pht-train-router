package de.difuture.ekut.pht.train.router.repository.trainroutes;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Enumerates all the routes that exist for a particular train.
 *
 * @author Lukas Zimmermann
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class TrainRoutes {

    @Id
    @JsonProperty("trainID")
    private UUID trainID;

    @ElementCollection(targetClass = Long.class, fetch = FetchType.EAGER)
    @JsonProperty("routes")
    private List<Long> routes;

    public TrainRoutes(final UUID trainID) {

        this.trainID = trainID;
        this.routes = new ArrayList<>();
    }
}
