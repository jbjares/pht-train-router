package de.difuture.ekut.pht.train.router.repository.traindestination;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.List;

public interface TrainDestinationRepository extends Neo4jRepository<TrainDestination, Long> {

    List<TrainDestination> findAllByCanBeVisitedIsTrueAndHasBeenVisitedIsFalse();

    List<TrainDestination> findAllByTrainIDAndRouteID(String trainID, Long routeID);

    List<TrainDestination> findAllByTrainID(String trainID);

    List<TrainDestination> findAllByTrainIDAndRootIsTrue(String trainID);

    /**
     * These nodes can be processed, because these nodes are Root nodes.
     * But the train is not available.
     */
    List<TrainDestination> findAllByRootIsTrueAndCanBeVisitedIsFalse();
}
