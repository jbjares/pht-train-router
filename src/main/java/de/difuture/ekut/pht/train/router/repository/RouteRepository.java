package de.difuture.ekut.pht.train.router.repository;


import de.difuture.ekut.pht.lib.core.neo4j.entity.Route;
import de.difuture.ekut.pht.lib.core.neo4j.entity.TrainDestination;
import org.springframework.data.neo4j.annotation.Depth;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RouteRepository extends Neo4jRepository<Route, Long> {

    @Query(" MATCH (r:Route)-[:STARTS_AT]->(x)-[:IS_PARENT_OF*]->(y)\n" +
           " WHERE ID(r)={routeID}\n" +
           " WITH collect(x) as parents, collect(y) as children\n" +
           " UNWIND parents + children as ids\n" +
           " RETURN collect(distinct ids)")
    @Depth(2)
    List<TrainDestination> getTrainDestinations(@Param("routeID") Long routeID);
}
