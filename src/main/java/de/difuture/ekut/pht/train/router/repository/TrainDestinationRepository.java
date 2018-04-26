package de.difuture.ekut.pht.train.router.repository;

import de.difuture.ekut.pht.lib.core.neo4j.entity.TrainDestination;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface TrainDestinationRepository extends Neo4jRepository<TrainDestination, Long> {

    List<TrainDestination> findAllByCanBeVisitedIsTrueAndHasBeenVisitedIsFalse();

    /**
     * These nodes can be processed, because these nodes are Root nodes.
     * But the train is not available.
     */
    List<TrainDestination> findAllByRootIsTrueAndCanBeVisitedIsFalse();
}
