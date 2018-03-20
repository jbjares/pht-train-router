package de.difuture.ekut.pht.trainrouter.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public final class TrainRoute {

	@Id
	private UUID trainId;

	@ElementCollection(fetch = FetchType.EAGER, targetClass = UUID.class)
	private List<UUID> queue;

	private int currentStation;


	public TrainRoute(final UUID trainId, final Iterable<Station> queue){

	    this.trainId = trainId;
	    this.currentStation = 0;
	    this.queue = new ArrayList<>();
	    queue.forEach(x -> this.queue.add(x.getId()));
    }


	public TrainRoute next(final Iterable<Station> moreStations) {

        // Add missing stations to list
        final List<UUID> stations = this.queue;

        // Add more stations if they do not already exist
        for (final Station nextStation : moreStations) {

            final UUID stationID = nextStation.getId();

            if ( ! stations.contains(stationID) ) {

                stations.add(stationID);
            }
        }
        return new TrainRoute(
                this.trainId,
                stations,
                this.currentStation + 1
        );
    }

    public Optional<UUID> getNextStation() {

	    return this.currentStation < this.queue.size() ?
                Optional.of(this.queue.get(this.currentStation)) : Optional.empty();

    }
}
