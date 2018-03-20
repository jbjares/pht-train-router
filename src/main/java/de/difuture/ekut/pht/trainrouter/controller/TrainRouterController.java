package de.difuture.ekut.pht.trainrouter.controller;


import ch.qos.logback.core.net.SyslogOutputStream;
import de.difuture.ekut.pht.lib.core.messages.TrainAvailable;
import de.difuture.ekut.pht.trainrouter.api.RouteResponse;
import de.difuture.ekut.pht.trainrouter.message.TrainUpdateStreams;
import de.difuture.ekut.pht.trainrouter.model.TrainRoute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.difuture.ekut.pht.trainrouter.repository.TrainRouteRepository;
import lombok.NonNull;

import java.util.Optional;
import java.util.UUID;

@RestController
public class TrainRouterController {
	
	//private final ReservedTrainRepository reservedtrainRepository;
	private final TrainRouteRepository trainRouteRepository;

	@StreamListener(TrainUpdateStreams.TRAIN_AVAILABLE)
	public void handleTrainAvailable(@Payload TrainAvailable message) {

	    final UUID trainID = message.getTrainID();
        System.out.println("MESSAGE " + trainID);

        final Optional<TrainRoute> route = this.trainRouteRepository.findById(trainID);
        this.trainRouteRepository.save(
                route.isPresent() ?
                            route.get().nextStop() : new TrainRoute(trainID, 0L)
        );
	}

	@Autowired
	public TrainRouterController(
			@NonNull final TrainRouteRepository trainRouteRepository) {

		this.trainRouteRepository = trainRouteRepository;
	}
	

    @RequestMapping(value = "/route", method = RequestMethod.GET)
    public RouteResponse route() {

	    return new RouteResponse(this.trainRouteRepository.findAll());
    }
}
