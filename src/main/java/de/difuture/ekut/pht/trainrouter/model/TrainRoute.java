package de.difuture.ekut.pht.trainrouter.model;

import java.util.Queue;
import java.util.UUID;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;

import lombok.Value;

@Value
@Entity
public class TrainRoute {

	@Id
	UUID trainId;
	
	@ElementCollection(fetch=FetchType.EAGER)
	Queue<UUID> stations;
}
