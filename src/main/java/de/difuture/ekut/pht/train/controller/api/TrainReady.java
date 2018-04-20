package de.difuture.ekut.pht.train.controller.api;


import lombok.Data;

import java.net.URI;
import java.util.UUID;

@Data
public class TrainReady {

    private URI repositoryURI;
    private UUID trainID;
    private UUID tag;
}
