package de.difuture.ekut.pht.train.controller.repository;

import de.difuture.ekut.pht.train.controller.model.Station;
import org.springframework.data.repository.CrudRepository;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

public interface StationRepository extends CrudRepository<Station, UUID> {

    Optional<Station> findStationByUri(final URI uri);
}
