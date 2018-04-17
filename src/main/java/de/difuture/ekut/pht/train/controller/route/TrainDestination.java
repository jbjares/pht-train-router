package de.difuture.ekut.pht.train.controller.route;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;
import java.util.UUID;


/**
 *  A train destination consists of a station and the trainID the
 *  trainDestination is associated with.
 *
 * @author Lukas Zimmermann
 */
@NodeEntity
public class TrainDestination {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    // Id of the station that constitutes the train destination
    Long tag;

    // Id of the train that is associated with this route
    UUID trainID;

    @Relationship(type="incoming", direction = Relationship.INCOMING)
    List<TrainDestination> incoming;

    @Relationship(type = "outgoing")
    List<TrainDestination> outgoing;

    // Whether the station has reported to have this train processed
    boolean processed;
}
