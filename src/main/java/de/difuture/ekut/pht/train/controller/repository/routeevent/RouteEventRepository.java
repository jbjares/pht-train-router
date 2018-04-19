package de.difuture.ekut.pht.train.controller.repository.routeevent;

import de.difuture.ekut.pht.train.controller.repository.station.Station;
import org.springframework.data.repository.CrudRepository;

public interface RouteEventRepository extends CrudRepository<Station, Long> {}
