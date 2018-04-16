package de.difuture.ekut.pht.train.controller.controller;


import de.difuture.ekut.pht.lib.core.messages.TrainAvailable;
import de.difuture.ekut.pht.lib.core.messages.TrainPassing;
import de.difuture.ekut.pht.train.controller.api.RouteResponse;
import de.difuture.ekut.pht.train.controller.api.StationResponse;
import de.difuture.ekut.pht.train.controller.message.TrainUpdateStreams;
import de.difuture.ekut.pht.train.controller.model.Station;
import de.difuture.ekut.pht.train.controller.model.TrainRoute;
import de.difuture.ekut.pht.train.controller.repository.StationRepository;
import de.difuture.ekut.pht.train.controller.repository.TrainRouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.NonNull;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class TrainControllerController {
	
	private final TrainRouteRepository trainRouteRepository;
	private final StationRepository stationRepository;

	private final TrainUpdateStreams trainUpdateStreams;
	private final DiscoveryClient discoveryClient;

    @Autowired
    public TrainControllerController(
            @NonNull final TrainRouteRepository trainRouteRepository,
            @NonNull final StationRepository stationRepository,
            @NonNull final TrainUpdateStreams trainUpdateStreams,
            @NonNull final DiscoveryClient discoveryClient) {

        this.trainRouteRepository = trainRouteRepository;
        this.stationRepository = stationRepository;
        this.trainUpdateStreams = trainUpdateStreams;
        this.discoveryClient = discoveryClient;
    }

    private void updateStations() {

        final List<Station> stations = this.discoveryClient.getInstances("station")
                .stream()
                .map( serviceInstance ->
                    new Station(serviceInstance.getUri())
        ).collect(Collectors.toList());

        // Safe stations if not already present
        for (final Station station: stations) {

            final URI uri = station.getUri();
            final Optional<Station> optStation
                    = this.stationRepository.findStationByUri(uri);
            if ( ! optStation.isPresent()) {

                this.stationRepository.save(new Station(uri));
            }
        }
    }


	@StreamListener(TrainUpdateStreams.TRAIN_AVAILABLE)
	public void handleTrainAvailable(@Payload TrainAvailable message) {

        // Update known stations form the service discovery
        this.updateStations();
        final Iterable<Station> stations = this.stationRepository.findAll();

        final UUID trainID = message.getTrainID();
	    final Optional<TrainRoute> routeOpt = this.trainRouteRepository.findById(trainID);
	    final Optional<UUID> nextStationOpt = this.trainRouteRepository.save(

	            routeOpt.isPresent() ?
                        routeOpt.get().next(stations) :
                        new TrainRoute(trainID, stations)
        ).getNextStation();

	    // If the TrainRoute has a next station, then let the train pass
        if (nextStationOpt.isPresent()) {

            final Optional<Station> n
                    = this.stationRepository.findById(nextStationOpt.get());
            this.broadcastTrainRoute(
                    new TrainPassing(
                            trainID,
                            n.get().getUri(),
                            n.get().getHost(),
                            n.get().getPort()));
        }
	}

    private boolean broadcastTrainRoute(final TrainPassing route) {

        return this.trainUpdateStreams.outboundTrack().send(
                MessageBuilder
                        .withPayload(route)
                        .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                        .build());
    }

    @RequestMapping(value = "/route", method = RequestMethod.GET)
    public RouteResponse route() {

	    return new RouteResponse(this.trainRouteRepository.findAll());
    }

    @RequestMapping(value = "/station", method = RequestMethod.GET)
    public StationResponse station() {

        return new StationResponse(this.stationRepository.findAll());
    }
}
