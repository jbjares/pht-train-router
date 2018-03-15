package de.difuture.ekut.pht.trainrouter.model;

import java.util.UUID;

import lombok.Data;

@Data
public final class TrainVisitRequest {

	private UUID stationID;
	private UUID trainID;
	private TrainVisitRequestMode mode;
}
