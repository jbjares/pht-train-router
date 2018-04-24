package de.difuture.ekut.pht.train.router.repository.routeevent;


import de.difuture.ekut.pht.lib.core.messages.TrainUpdate;
import de.difuture.ekut.pht.lib.core.model.Train;
import de.difuture.ekut.pht.lib.core.traintag.TrainTag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.net.URI;
import java.time.Instant;
import java.util.UUID;


/**
 * Model of all events that are received by the Train Controller.
 * Currently, these are mapped from TrainUpdate messages
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
    @Convert(converter = TrainTagConverter.class)
    private TrainTag tag;

    // TrainID that this train belongs to
    private UUID trainID;

    // docker Registry URI of the train
    private URI dockerRegistryURI;

    // Whether this routeEvent has been visited
    private boolean processed;

    // When this event has been visited
    private Instant processedInstant;

    public RouteEvent(final TrainUpdate trainUpdate) {

        this.trainID = trainUpdate.getTrainID();
        this.dockerRegistryURI = trainUpdate.getTrainRegistryURI();
        this.tag = TrainTag.of(trainUpdate.getTrainTag());
        this.processed = false;
    }

    public Train toTrain() {

        return new Train(this.trainID, this.dockerRegistryURI);
    }
}
