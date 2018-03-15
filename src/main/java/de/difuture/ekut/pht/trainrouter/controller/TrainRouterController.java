package de.difuture.ekut.pht.trainrouter.controller;

import java.util.*;

import de.difuture.ekut.pht.trainrouter.api.RouteResponse;
import de.difuture.ekut.pht.trainrouter.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.difuture.ekut.pht.trainrouter.repository.ReservedTrainRepository;
import de.difuture.ekut.pht.trainrouter.repository.TrainRouteRepository;
import lombok.NonNull;
import java.util.concurrent.ThreadLocalRandom;


@RestController
public class TrainRouterController {
	
	private final ReservedTrainRepository reservedtrainRepository;
	private final TrainRouteRepository trainRouteRepository;
	
	private static final ResponseEntity<TrainVisitResponse> NOT_FOUND = ResponseEntity.notFound().build();
	private static final ResponseEntity<TrainVisitResponse> OK = ResponseEntity.ok().build();
	
	@Autowired
	public TrainRouterController(
			@NonNull final ReservedTrainRepository reservedtrainRepository,
			@NonNull final TrainRouteRepository trainRouteRepository) {
		
		this.reservedtrainRepository = reservedtrainRepository;
		this.trainRouteRepository = trainRouteRepository;
	}
	
	private TrainRoute ensureEnqueued(TrainVisitRequest request) {

        final UUID stationUUID = request.getStationID();
        final UUID trainID = request.getTrainID();

        final Optional<TrainRoute> oroute = this.trainRouteRepository.findById(trainID);

		if (oroute.isPresent()) {

		    final TrainRoute route = oroute.get();
			final Queue<UUID> stations = route.getStations();
			if ( ! stations.contains(stationUUID)) {

			    stations.add(stationUUID);
            }
            return this.trainRouteRepository.save(route);
		}

		final Queue<UUID> queue = new ArrayDeque<>();
		queue.add(stationUUID);

		return this.trainRouteRepository.save(new TrainRoute(trainID, queue));
	}

	@RequestMapping(value = "/request", method = RequestMethod.POST)
	public ResponseEntity<TrainVisitResponse> request(
			@RequestBody TrainVisitRequest trainVisitRequest) {
		
		final TrainVisitRequestMode mode = trainVisitRequest.getMode();

		// TODO Switch on mode, currently enqueue only for concrete train ID allowed
		if (mode == TrainVisitRequestMode.ID) {

		    // TODO Always grant acess to train for now
		    final TrainRoute route =  this.ensureEnqueued(trainVisitRequest);
		    return ResponseEntity.ok(new TrainVisitResponse(route.getTrainId(), TrainVisitResponseMode.GRANTED));
		}
		return OK;
	}

    @RequestMapping(value = "/route", method = RequestMethod.GET)
    public RouteResponse route() {

	    return new RouteResponse(this.trainRouteRepository.findAll());
    }
}
