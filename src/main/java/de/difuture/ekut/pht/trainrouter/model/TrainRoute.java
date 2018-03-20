package de.difuture.ekut.pht.trainrouter.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Data
@NoArgsConstructor
@Entity
public final class TrainRoute {

	@Id
	private UUID trainId;
	private Long number;

	public TrainRoute(
			@NonNull final UUID id,
			@NonNull final Long number) {

		this.trainId = id;
		this.number = number;
	}

	public TrainRoute nextStop() {

		return new TrainRoute(this.trainId, this.number + 1);
	}
}
