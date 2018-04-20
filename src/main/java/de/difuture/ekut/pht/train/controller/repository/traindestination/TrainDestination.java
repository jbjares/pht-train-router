package de.difuture.ekut.pht.train.controller.repository.traindestination;

import de.difuture.ekut.pht.train.controller.repository.station.Station;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.*;


/**
 *  A train destination consists of a station and the trainID the
 *  trainDestination is associated with.
 *
 * @author Lukas Zimmermann
 */
@NodeEntity
@Setter
@NoArgsConstructor
public class TrainDestination {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Tag that the station has been assigned with, must be an UUID. This is the same as
    // the Tag of the Docker Image
    private UUID stationID;

    // Id of the train that is associated with this route
    private UUID trainID;

    @Relationship(type = "outgoing")
    private List<TrainDestination> outgoing;

    // Whether the station has reported to have this train visited
    private boolean visited;

    public void addOutgoing(final TrainDestination trainDestination) {

        this.outgoing.add(trainDestination);
    }

    private TrainDestination(
            UUID stationID,
            UUID trainID,
            List<TrainDestination> incoming,
            List<TrainDestination> outgoing) {

        this.stationID = stationID;
        this.trainID = trainID;

        // Safe copy of incoming and outgoing links
        this.outgoing = new ArrayList<>(outgoing);
    }

    public static TrainDestination of(Station station, UUID trainID) {

        return new TrainDestination(
                station.getId(),
                trainID,
                new ArrayList<>(),
                new ArrayList<>());
    }

    public static Optional<TrainDestination> of(Iterable<Station> stations, UUID trainID) {

        final Iterator<Station> iterator = stations.iterator();

        if ( ! iterator.hasNext()) {

            return Optional.empty();
        }

        TrainDestination headDestination = TrainDestination.of(iterator.next(), trainID);
        TrainDestination result = headDestination;
        while (iterator.hasNext()) {

            final TrainDestination childDestination = TrainDestination.of(iterator.next(), trainID);
            headDestination.setOutgoing(Collections.singletonList(childDestination));
            headDestination = childDestination;
        }
        return Optional.of(result);
    }
}
