package de.difuture.ekut.pht.train.router.repository.routeevent;


import de.difuture.ekut.pht.lib.core.messages.TrainAvailable;
import de.difuture.ekut.pht.lib.core.model.Train;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.net.URI;
import java.time.Instant;
import java.util.UUID;


/**
 * Model of all events that are received by the Train Controller.
 * Currently, these are mapped from TrainAvailable messages
 *
 * @author Lukas Zimmermann
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Repository trainTag that the new image was checked in with
    private String tag;

    // TrainID that this train belongs to
    private UUID trainID;

    // docker Registry URI of the train
    private URI dockerRegistryURI;

    // Whether this routeEvent has been visited
    private boolean processed;

    // When this event has been visited
    private Instant processedInstant;

    public RouteEvent(final TrainAvailable trainAvailable) {

        this.trainID = trainAvailable.getTrainID();
        this.dockerRegistryURI = trainAvailable.getTrainRegistryURI();
        this.tag = trainAvailable.getTrainTag();
        this.processed = false;
    }

    public Train toTrain() {

        return new Train(this.trainID, this.dockerRegistryURI);
    }
}
