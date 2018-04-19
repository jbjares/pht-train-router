package de.difuture.ekut.pht.train.controller.repository.routeevent;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    // Repository tag that the new image was checked in with
    private String tag;

    // TrainID that this train belongs to
    private UUID trainID;

    private boolean processed;

    public RouteEvent(UUID trainID, String tag) {

        this.trainID = trainID;
        this.tag = tag;
    }
}
