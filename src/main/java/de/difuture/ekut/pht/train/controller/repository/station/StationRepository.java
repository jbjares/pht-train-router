package de.difuture.ekut.pht.train.controller.repository.station;

import org.springframework.data.jpa.repository.JpaRepository;

import java.net.URI;
import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, Long> {

    Optional<Station> findStationByUri(final URI uri);
}
