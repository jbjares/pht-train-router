package de.difuture.ekut.pht.trainrouter.model;

import lombok.Value;

import java.util.UUID;

@Value
public class TrainVisitResponse {

    UUID trainID;

    TrainVisitResponseMode mode;
}
