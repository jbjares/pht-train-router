package de.difuture.ekut.pht.train.router.repository;

import de.difuture.ekut.pht.lib.core.neo4j.entity.Train;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface TrainRepository extends Neo4jRepository<Train, Long> {}
