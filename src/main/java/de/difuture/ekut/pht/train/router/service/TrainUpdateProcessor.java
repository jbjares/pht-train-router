package de.difuture.ekut.pht.train.router.service;


import de.difuture.ekut.pht.lib.core.messages.TrainUpdate;
import de.difuture.ekut.pht.lib.core.messages.TrainVisit;
import de.difuture.ekut.pht.lib.core.neo4j.entity.Train;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URI;


/**
 * Component scans events happening on the trainroutes and
 *
 */
@Component
public class TrainUpdateProcessor {

    private final RouteService routeService;
    private final Processor processor;

    private static boolean isLong(String value) {

        try {
            Long.valueOf(value);
            return true;

        } catch (NumberFormatException e){

            return false;
        }
    }

    @Autowired
    public TrainUpdateProcessor(
            RouteService routeService,
            Processor processor) {

        this.routeService = routeService;
        this.processor = processor;
    }


    @StreamListener(Processor.INPUT)
    public void processor(TrainUpdate trainUpdate) {

        final String tag = trainUpdate.getTrainTag();
        final Long trainID = trainUpdate.getTrainID();
        final URI trainRegistryURI = trainUpdate.getTrainRegistryURI();

        if ("START".equals(tag) && trainRegistryURI != null) {

            this.routeService.enableRoutes(trainID);

            // If the tag is long, we can update the corresponding TrainDestination and children accordingly
        } else if (isLong(tag)) {

            this.routeService.visitTrainDestination(Long.valueOf(tag));
        }
    }


    @Scheduled(fixedDelay = 3000)
    private void publishTrainVisits() {

        this.routeService
                .getPendingTrainDestinations()
                .forEach(trainDestination -> {

                    final Train train = trainDestination.getTrain();

                    // Determine tag. A root node must pull from "START", the remainder from
                    // the previous trainDestination
                    final String fromTag = trainDestination.isRoot() ? "START" :
                            trainDestination.getParent().getTrainDestinationID().toString();

                    this.processor.output().send(
                            MessageBuilder
                                    .withPayload(
                                            new TrainVisit(
                                                    train.getTrainID(),
                                                    trainDestination.getStation().getStationURI(),
                                                    train.getTrainRegistryURI(),
                                                    fromTag,
                                                    trainDestination.getTrainDestinationID().toString()))
                                    .build());
                        }
                );
    }
}
