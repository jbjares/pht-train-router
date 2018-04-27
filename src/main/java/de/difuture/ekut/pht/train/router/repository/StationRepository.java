package de.difuture.ekut.pht.train.router.repository;


import de.difuture.ekut.pht.lib.core.neo4j.entity.Station;
import org.springframework.data.neo4j.repository.Neo4jRepository;


public interface StationRepository extends Neo4jRepository<Station, Long> {}
