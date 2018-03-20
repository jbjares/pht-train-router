package de.difuture.ekut.pht.trainrouter.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;
import de.difuture.ekut.pht.trainrouter.model.TrainRoute;

public interface TrainRouteRepository extends CrudRepository<TrainRoute, UUID> {}
