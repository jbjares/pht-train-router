package de.difuture.ekut.pht.train.router.repository.traindestination;

import de.difuture.ekut.pht.lib.core.model.Station;
import de.difuture.ekut.pht.lib.core.model.Train;
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

    // Id of the trainrouteassignment that this node belongs to
    private Long routeID;

    // Tag that the station has been assigned with, must be an UUID. This is the same as
    // the Tag of the Docker Image
    private String stationID;

    // Id of the train that is associated with this trainrouteassignment
    private String trainID;

    // The trainRegistry this train belongs to
    private String trainDockerRegistryURI;

    @Relationship(type = "IS_PARENT_OF")
    private List<TrainDestination> children;

    @Relationship(type = "IS_CHILD_OF")
    private List<TrainDestination> parents;

    // Whether the station has reported to have this train visited
    private boolean hasBeenVisited;

    // Whether the station is ready to be visited
    private boolean canBeVisited;

    private TrainDestination(UUID stationID, Train train, Long routeID) {

        this.stationID = stationID.toString();
        this.trainID = train.getTrainID().toString();
        this.trainDockerRegistryURI = train.getTrainRegistryURI().toString();
        this.routeID = routeID;
        this.canBeVisited = false;
        this.hasBeenVisited = false;
    }

    private static TrainDestination of(
            Station station,
            Train train,
            Long routeID) {

        return new TrainDestination(station.getStationID(), train, routeID);
    }


    public static Optional<TrainDestination> of(
            Iterable<Station> stations,
            Train train,
            Long routeID) {

        final Iterator<Station> iterator = stations.iterator();

        if ( ! iterator.hasNext()) {

            return Optional.empty();
        }
        TrainDestination parent = TrainDestination.of(
                iterator.next(),
                train, routeID);
        final TrainDestination head = parent;

        while (iterator.hasNext()) {

            final TrainDestination child = TrainDestination.of(
                    iterator.next(),
                    train, routeID);
            parent.setChildren(Collections.singletonList(child));
            child.setParents(Collections.singletonList(parent));
            parent = child;
        }
        head.setCanBeVisited(true);
        return Optional.of(head);
    }
}
