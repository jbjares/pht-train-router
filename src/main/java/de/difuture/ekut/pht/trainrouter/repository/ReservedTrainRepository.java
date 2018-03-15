package de.difuture.ekut.pht.trainrouter.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import de.difuture.ekut.pht.trainrouter.model.ReservedTrain;

public interface ReservedTrainRepository extends CrudRepository<ReservedTrain, UUID> {}
