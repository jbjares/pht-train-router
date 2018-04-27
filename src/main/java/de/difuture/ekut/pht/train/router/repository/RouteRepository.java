package de.difuture.ekut.pht.train.router.repository;


import de.difuture.ekut.pht.lib.core.neo4j.entity.Route;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface RouteRepository extends Neo4jRepository<Route, Long> {}
