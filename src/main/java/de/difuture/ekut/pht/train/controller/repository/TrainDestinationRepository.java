package de.difuture.ekut.pht.train.controller.repository;

import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface TrainDestinationRepository extends Neo4jRepository<TrainDestination, Long> {}
