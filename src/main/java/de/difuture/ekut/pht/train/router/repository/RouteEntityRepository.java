package de.difuture.ekut.pht.train.router.repository;


import de.difuture.ekut.pht.lib.core.neo4j.entity.RouteEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface RouteEntityRepository extends Neo4jRepository<RouteEntity, Long> {}
