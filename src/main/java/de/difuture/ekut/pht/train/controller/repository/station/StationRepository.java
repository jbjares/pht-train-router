package de.difuture.ekut.pht.train.controller.repository.station;

import org.springframework.data.repository.CrudRepository;

import java.net.URI;
import java.util.Optional;

public interface StationRepository extends CrudRepository<Station, Long> {

    Optional<Station> findStationByUri(final URI uri);
}
