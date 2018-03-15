package de.difuture.ekut.pht.trainrouter.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Value;

@Value
@Entity
public class ReservedTrain {

	@Id
	UUID trainID;
	UUID stationId;
}
