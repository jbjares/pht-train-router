package de.difuture.ekut.pht.trainrouter.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public final class TrainVisit {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private UUID id;
	private UUID trainID;
	private UUID stationID;
	private TrainVisitState state;
}
