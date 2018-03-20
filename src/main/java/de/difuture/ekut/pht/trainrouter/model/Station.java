package de.difuture.ekut.pht.trainrouter.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.net.URI;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Data
public final class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private URI uri;
    private String host;
    private int port;

    public Station(URI uri) {

        this.uri = uri;
        this.host = uri.getHost();
        this.port = uri.getPort();
    }
}
