package de.difuture.ekut.pht.train.router.repository.traindestination;

import de.difuture.ekut.pht.train.router.repository.station.Station;
import lombok.Getter;
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
@Getter
@NoArgsConstructor
public class TrainDestination {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Tag that the station has been assigned with, must be an UUID. This is the same as
    // the Tag of the Docker Image
    private String stationID;

    // Id of the train that is associated with this route
    private String trainID;

    @Relationship(type = "IS_PARENT_OF")
    private List<TrainDestination> children;

    @Relationship(type = "IS_CHILD_OF")
    private List<TrainDestination> parents;

    // Whether the station has reported to have this train visited
    private boolean hasBeenVisited;

    // Whether the station is ready to be visited
    private boolean canBeVisited;

    private TrainDestination(
            UUID stationID,
            UUID trainID) {

        this.stationID = stationID.toString();
        this.trainID = trainID.toString();
        this.canBeVisited = false;
        this.hasBeenVisited = false;
    }

    public static TrainDestination of(Station station, UUID trainID) {

        return new TrainDestination(station.getId(), trainID);
    }

    public static Optional<TrainDestination> of(Iterable<Station> stations, UUID trainID) {

        final Iterator<Station> iterator = stations.iterator();

        if ( ! iterator.hasNext()) {

            return Optional.empty();
        }
        TrainDestination parent = TrainDestination.of(iterator.next(), trainID);
        final TrainDestination head = parent;

        while (iterator.hasNext()) {

            final TrainDestination child = TrainDestination.of(iterator.next(), trainID);
            parent.setChildren(Collections.singletonList(child));
            child.setParents(Collections.singletonList(parent));
            parent = child;
        }
        head.setCanBeVisited(true);
        return Optional.of(head);
    }
}
