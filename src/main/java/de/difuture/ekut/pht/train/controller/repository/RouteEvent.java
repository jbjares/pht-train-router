package de.difuture.ekut.pht.train.controller.repository;


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

    private String tag;

    private UUID trainID;

    private boolean processed;

    public RouteEvent(String tag, UUID trainID) {

        this.tag = tag;
        this.trainID = trainID;
        this.processed = false;
    }
}
