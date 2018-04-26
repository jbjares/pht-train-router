package de.difuture.ekut.pht.train.router.repository;

import de.difuture.ekut.pht.lib.core.neo4j.entity.TrainEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface TrainEntityRepository extends Neo4jRepository<TrainEntity, Long> {}
