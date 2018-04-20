package de.difuture.ekut.pht.train.controller.repository.station;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.time.Instant;
import java.util.UUID;

/**
 *
 * Represents a station as the train controller sees stations. The train controller
 * sees the station only as an URI, so it can notify stations via WebHooks.
 *
 * Also, a station has to register all the URIs that it will use for pushing the
 * Image back to the registry.
 *
 * @author Lukas Zimmermann
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @NotNull
    private URI uri;

    // When the station has last sent a ping to the train controller
    private Instant last_ping;

    // Whether the station is currently enabled
    private boolean enabled;
}
