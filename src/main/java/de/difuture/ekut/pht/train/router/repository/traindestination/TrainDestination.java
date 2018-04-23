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
import java.util.stream.Collectors;


/**
 *  A TrainDestination is a node in a route for a particular train.
 *  It consists of the following attributes
 *
 *  * id. The internal ID for the Neo4j Persistence
 *  * routeID. The id of the route that this node belongs to. NodeIDs are train specific.
 *  * stationID. The ID of the station that this node addresses.
 *  * trainID. The ID of the train that this node belongs to
 *  * trainDockerRegistryURI the URI of the DockerTrainRegistry that this train belongs to
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

    // Id of the trainroutes that this node belongs to
    private Long routeID;

    // Tag that the station has been assigned with, must be an UUID. This is the same as
    // the Tag of the Docker Image
    private String stationID;

    // Id of the train that is associated with this trainroutes
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
            final UUID stationID,
            final UUID trainID,
            final Long routeID) {

        this.stationID = stationID.toString();
        this.trainID = trainID.toString();
        this.routeID = routeID;
        this.children = new ArrayList<>();
        this.parents = new ArrayList<>();
        this.canBeVisited = false;
        this.hasBeenVisited = false;
    }

    public static TrainDestination of(UUID stationID, UUID trainID, Long routeID) {

        return new TrainDestination(stationID, trainID, routeID);
    }

    /**
     *
     * Establishes IS_PARENT_OF and IS_CHILD_OF relationship between the two provided train destinations.
     * This method will not have any effect if the relationship between parent and child already exists.
     *
     * @param parent The parent of the new relationship
     *
     * @param child The child of the new relationship
     */
    public static void link(final TrainDestination parent, final TrainDestination child) {

        final List<TrainDestination> children = parent.getChildren();
        final List<TrainDestination> parents = child.getParents();

        final Set<String> childrenIDs = children.stream().map(TrainDestination::getStationID).collect(Collectors.toSet());
        final Set<String> parentIDs = parents.stream().map(TrainDestination::getStationID).collect(Collectors.toSet());

        if ( ! childrenIDs.contains(child.getId())) {

            children.add(child);
            parent.setChildren(children);
        }

        if ( ! parentIDs.contains(parent.getId())) {

            parents.add(parent);
            child.setParents(parents);
        }
    }


    public static TrainDestination of(
            Station station,
            Train train,
            Long routeID) {

        return new TrainDestination(station.getStationID(), train.getTrainID(), routeID);
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
