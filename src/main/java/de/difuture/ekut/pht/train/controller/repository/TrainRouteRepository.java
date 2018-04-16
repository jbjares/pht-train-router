package de.difuture.ekut.pht.train.controller.repository;

import java.util.UUID;

import de.difuture.ekut.pht.train.controller.model.TrainRoute;
import org.springframework.data.repository.CrudRepository;

public interface TrainRouteRepository extends CrudRepository<TrainRoute, UUID> {}
